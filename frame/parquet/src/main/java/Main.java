import org.apache.avro.reflect.ReflectData;
import org.apache.parquet.ParquetReadOptions;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.column.ColumnDescriptor;
import org.apache.parquet.column.page.PageReadStore;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.convert.GroupRecordConverter;
import org.apache.parquet.hadoop.ParquetFileReader;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.metadata.ParquetMetadata;
import org.apache.parquet.io.ColumnIOFactory;
import org.apache.parquet.io.LocalInputFile;
import org.apache.parquet.io.LocalOutputFile;
import org.apache.parquet.io.RecordReader;
import org.apache.parquet.schema.MessageType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final Path BASE_PATH = Paths.get("base.parquet");

    @Test
    public void write() throws IOException {
        try (ParquetWriter<Entry> writer = AvroParquetWriter.<Entry>builder(new LocalOutputFile(BASE_PATH))
                .withSchema(ReflectData.AllowNull.get().getSchema(Entry.class))
                .withDataModel(ReflectData.get())
                .withCompressionCodec(CompressionCodecName.SNAPPY)
                .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
                .build()) {
            for (int i = 0; i < 100; i++) {
                writer.write(new Entry("str" + i, i, i * 1.0));
            }
        }
    }

    @Test
    public void readByEntry() throws IOException {
        try (ParquetReader reader = AvroParquetReader.<Entry>builder(new LocalInputFile(BASE_PATH))
                .withDataModel(ReflectData.get())
                .disableCompatibility()
                .build()) {
            Entry entry;
            while ((entry = (Entry) reader.read()) != null) {
                System.out.println(entry);
            }
        }
    }

    @Test
    public void readByGeneral() throws IOException {
        ParquetReadOptions options = ParquetReadOptions.builder().build();
        LocalInputFile inputFile = new LocalInputFile(BASE_PATH);
        ParquetMetadata metadata = ParquetFileReader.readFooter(inputFile, options, inputFile.newStream());
        MessageType schema = metadata.getFileMetaData().getSchema();
        System.out.println("----columns----");
        List<ColumnDescriptor> columns = schema.getColumns();
        columns
                .stream()
                .map(ColumnDescriptor::getPath)
                .map(strings -> String.join("->", strings))
                .forEach(System.out::println);
        System.out.println("----data----");
        try (ParquetFileReader reader = new ParquetFileReader(inputFile, options)) {
            PageReadStore pageReadStore;
            int page = 0;
            while ((pageReadStore = reader.readNextRowGroup()) != null) {
                long rowCount = pageReadStore.getRowCount();
                System.out.println("----page: " + (page++) + " count: " + rowCount + "----");
                RecordReader<Group> recordReader = new ColumnIOFactory()
                        .getColumnIO(schema)
                        .getRecordReader(pageReadStore, new GroupRecordConverter(schema));
                for (long i = 0; i < rowCount; i++) {
                    Group group = recordReader.read();
                    String data = columns.stream()
                            .map(columnDescriptor -> group.getValueToString(group.getType().getFieldIndex(columnDescriptor.getPath()[0]), 0))
                            .collect(Collectors.joining(","));
                    System.out.println(data);
                }
            }
        }
    }

}
