package Jfame_text;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.List;

/**
 * 文件拖拽、文件属性
 */
public class DNDFileTest extends JFrame {

    public static void main(String[] args) {
        JFrame jFrame = new DNDFileTest();
        jFrame.setVisible(true);
    }

    public DNDFileTest() throws HeadlessException {
        setBounds(160, 90, 1600, 900);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        final TextArea textArea = new TextArea();
        textArea.setBackground(new Color(200, 200, 200));

        new DropTarget(textArea, DnDConstants.ACTION_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                textArea.setBackground(new Color(200, 200, 200));
                try {
                    @SuppressWarnings("unchecked")
                    List<File> list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    textArea.setText("文件数:"+list.size());
                    list.forEach(file -> {
                        Path path = Paths.get(file.toURI());
                        try {
                            DosFileAttributes attributes = Files.getFileAttributeView(path, DosFileAttributeView.class).readAttributes();
                            textArea.append("\n文件路径:"+path.toUri().getPath()+"\n");
                            textArea.append("文件大小:"+attributes.size()+"\n");
                            textArea.append("创建时间:"+attributes.creationTime()+"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    dtde.dropComplete(true);
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                super.dragOver(dtde);
                textArea.setBackground(new Color(200, 250, 200));
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                super.dragExit(dte);
                textArea.setBackground(new Color(200, 200, 200));
            }
        });

        panel.add(textArea, JLabel.CENTER);

        add(panel, JLabel.CENTER);



    }

}
