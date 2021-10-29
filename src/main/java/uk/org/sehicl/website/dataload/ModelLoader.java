package uk.org.sehicl.website.dataload;

import java.io.InputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.data.Model;

public class ModelLoader
{
    private static FileLoader fileLoader = new ClasspathFileLoader();
    private static final Map<Integer, Model> models = new HashMap<>();
    public final static int DEFAULT_SEASON = Constants.CURRENT_SEASON;

    public static void setFileLoader(FileLoader loader)
    {
        fileLoader = loader;
    }

    public static Model getModel()
    {
        return getModel(DEFAULT_SEASON);
    }

    public static Model getModel(int season)
    {
        Model answer = models.get(season);
        if (answer == null)
        {
            String fileName = String.format("data/20%02d-%02d.xml", season - 1, season);
            InputStream str = fileLoader.getStream(fileName);
            try (InputStream source = str)
            {
                ObjectMapper xmlMapper = new XmlMapper();
                xmlMapper.setTimeZone(TimeZone.getDefault());
                Function<InputStream, Reader> transformer = transformer(season);
                if (transformer != null)
                    answer = xmlMapper.readValue(transformer.apply(source), Model.class);
                else
                    answer = xmlMapper.readValue(source, Model.class);
            }
            catch (Exception ex)
            {
                throw new RuntimeException(String.format("Unable to load model file %s", fileName),
                        ex);
            }
        }
        return answer;
    }

    private static Function<InputStream, Reader> transformer(int season)
    {
        String fileName = String.format("xsl/20%02d-%02d.xslt", season - 1, season);
        InputStream str = fileLoader.getStream(fileName);
        if (str == null)
            return null;
        try
        {
            Transformer transformer = TransformerFactory
                    .newDefaultInstance()
                    .newTransformer(new StreamSource(str));
            return is -> transform(transformer, is);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(String.format("Unable to load stylesheet %s", fileName), ex);
        }
    }

    private static Reader transform(Transformer transformer, InputStream is)
    {
        PipedWriter w = new PipedWriter();
        StreamResult result = new StreamResult(w);
        try
        {
            PipedReader r = new PipedReader(w);
            ForkJoinPool.commonPool().execute(() ->
            {
                try
                {
                    transformer.transform(new StreamSource(is), result);
                }
                catch (Exception ex)
                {
                    throw new RuntimeException("Unable to transform data", ex);
                }
            });
            return r;
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Unable to transform data", ex);
        }
    }
}
