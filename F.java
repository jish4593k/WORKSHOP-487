import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PdfOperations {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        mergeFiles(scanner);
        compressFiles(scanner);
        splitFile(scanner);

        scanner.close();
    }

    private static List<String> getFiles(Scanner scanner) {
        List<String> files = new ArrayList<>();
        boolean nextFile = true;

        while (nextFile) {
            System.out.print("Enter the path of the file: ");
            files.add(scanner.nextLine());

            System.out.print("Do you want to continue? (y=yes/n=No): ");
            nextFile = scanner.nextLine().equalsIgnoreCase("y");
        }

        return files;
    }

    private static void mergeFiles(Scanner scanner) {
        try {
            List<String> filesToMerge = getFiles(scanner);

            if (filesToMerge.size() > 1) {
                PDDocument mergedDocument = new PDDocument();

                for (String fileToMerge : filesToMerge) {
                    PDDocument document = PDDocument.load(new java.io.File(fileToMerge));
                    mergedDocument.addDocument(document);
                }

                System.out.print("Enter the name of the merged PDF file: ");
                String resultFilename = scanner.nextLine();
                mergedDocument.save(resultFilename);
                mergedDocument.close();

                System.out.println("Merge Successful!");
            } else {
                System.out.println("You need at least 2 files to merge.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void compressFiles(Scanner scanner) {
        try {
            List<String> filesToCompress = getFiles(scanner);
            PDDocument compressedDocument = new PDDocument();

            for (String fileToCompress : filesToCompress) {
                PDDocument document = PDDocument.load(new java.io.File(fileToCompress));

                for (PDPage page : document.getPages()) {
                    page.setContents(null); // Compress content streams
                }

                compressedDocument.addDocument(document);
            }

            System.out.print("Enter the name of the compressed PDF file: ");
            String resultFilename = scanner.nextLine();
            compressedDocument.save(resultFilename);
            compressedDocument.close();

            System.out.println("Compression Successful!");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void splitFile(Scanner scanner) {
        try {
            System.out.print("Enter the path of the PDF file to split: ");
            String filePath = scanner.nextLine();

            System.out.print("Enter the start page number: ");
            int startPage = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter the end page number: ");
            int endPage = Integer.parseInt(scanner.nextLine());

            PDDocument document = PDDocument.load(new java.io.File(filePath));
            PDDocument splitDocument = new PDDocument();

            for (int i = startPage - 1; i < Math.min(endPage, document.getNumberOfPages()); i++) {
                splitDocument.addPage(document.getPage(i));
            }

            System.out.print("Enter the name of the split PDF file: ");
            String resultFilename = scanner.nextLine();
            splitDocument.save(resultFilename);
            splitDocument.close();
            document.close();

            System.out.println("Split Successful!");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
