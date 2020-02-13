import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    @Test
    public void writeOneSheet() {
        EasyExcel
                .write("one.xlsx")
                .sheet("test")
                .doWrite(Stream.iterate(1, i -> i + 1)
                        .limit(20)
                        .map(i -> List.of("A" + i, "B" + i, "C" + i, "D" + i))
                        .collect(Collectors.toList()));
    }

    @Test
    public void writeMoreSheet() {
        ExcelWriter excel = EasyExcel
                .write("more.xlsx")
                .build();
        for (int i = 0; i < 5; i++) {
            String sheetName = "Sheet" + i;
            excel.write(
                    Stream.iterate(1, j -> j + 1)
                            .limit(20)
                            .map(j -> List.of(sheetName + "A" + j, sheetName + "B" + j, sheetName + "C" + j, sheetName + "D" + j))
                            .collect(Collectors.toList()),
                    EasyExcel.writerSheet(i, "Sheet" + i).build()
            );
        }
        excel.finish();
    }

    @Test
    public void readOneSheet() {
        EasyExcel
                .read("one.xlsx")
                .registerReadListener(new AnalysisEventListener<Map<Integer, String>>() {
                    @Override
                    public void invoke(Map<Integer, String> data, AnalysisContext context) {
                        System.out.println(data);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("doAfterAllAnalysed");
                    }
                })
                .doReadAll();
    }

    @Test
    public void readMoreSheet() {
        EasyExcel
                .read("more.xlsx")
                .registerReadListener(new AnalysisEventListener<Map<Integer, String>>() {
                    @Override
                    public void invoke(Map<Integer, String> data, AnalysisContext context) {
                        System.out.println(data);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        System.out.println("SheetName:" + context.readSheetHolder().getSheetName());
                    }
                })
                .doReadAll();
    }

    @Test
    public void transfer() {
        ExcelWriter excel = EasyExcel.write("transfer.xlsx").build();
        EasyExcel
                .read("more.xlsx")
                .registerReadListener(new AnalysisEventListener<Map<Integer, String>>() {
                    private List<List<String>> sheet = new ArrayList<>();
                    @Override
                    public void invoke(Map<Integer, String> data, AnalysisContext context) {
                        sheet.add(data
                                .entrySet()
                                .stream()
                                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                                .map(Map.Entry::getValue)
                                .collect(Collectors.toList())
                        );
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        excel.write(sheet, EasyExcel.writerSheet(context.readSheetHolder().getSheetName()).build());
                        sheet = new ArrayList<>();
                    }
                })
                .doReadAll();
        excel.finish();
    }


}
