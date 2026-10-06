package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.educa.entity.ProductoEntity;
import org.educa.entity.SummaryEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public Productos readXml(String fileXml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Productos) unmarshaller.unmarshal(new File(fileXml));
    }

    @Override
    public void writeFile(SummaryEntity summaryEntity) {
        try (FileWriter fileWriter = new FileWriter(summaryEntity.getFileName())) {
            fileWriter.write(summaryEntity.toPrint());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportExcelDAO(String path, List<ProductoEntity> productos) throws IOException {
        // We create the book and file of Excel
        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja = libro.createSheet("ResumenInventario");

        // Calling teh auxiliar methods
        createHeaderRow(hoja);
        fillDataRows(hoja, productos);

        // We generate the dynamic file name requested in the instructions
        String timestamp = String.valueOf(System.currentTimeMillis());
        String fileName = "export_" + timestamp + ".xlsx";

        saveExcelFile(libro, path, fileName);
    }

    /**
     * Creates the first row of the Excel being the tittles of the columns.
     *
     * @param hoja is the Excel file.
     */
    private void createHeaderRow(XSSFSheet hoja) {
        XSSFWorkbook wb = hoja.getWorkbook();
        XSSFCellStyle headerStyle = wb.createCellStyle();

        // Setting up the font to bold
        XSSFFont font = wb.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Aligning the text to the center of the cell
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // Applying the green borders using the auxiliar method
        applyGreenBorder(headerStyle);

        // Creating the first row, index 0
        XSSFRow filaCabecera = hoja.createRow(0);
        String[] titulos = {"Código", "Marca", "Modelo", "Categoría", "Precio Final", "Coste", "Beneficio"};

        // Iterating through the titles array to create and style each cell
        for (int i = 0; i < titulos.length; i++) {
            XSSFCell celda = filaCabecera.createCell(i);
            celda.setCellValue(titulos[i]);
            celda.setCellStyle(headerStyle);
        }
    }

    /**
     * This method fills the rows with new cells and putting the data.
     *
     * @param hoja      is the Excel file where the data will be added.
     * @param productos is a list of products entity with all the information.
     */
    private void fillDataRows(XSSFSheet hoja, List<ProductoEntity> productos) {
        XSSFWorkbook wb = hoja.getWorkbook();
        XSSFFont fontBold = wb.createFont();
        fontBold.setBold(true);

        // We create the styles, being 0 the white and 1 the green, all this in an array
        XSSFCellStyle[] estilos = {wb.createCellStyle(), wb.createCellStyle()};
        estilos[1].setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        estilos[1].setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Styles with the black text for the first column
        XSSFCellStyle[] estilosBold = {wb.createCellStyle(), wb.createCellStyle()};

        for (int i = 0; i < 2; i++) {
            estilos[i].setAlignment(HorizontalAlignment.CENTER);
            applyGreenBorder(estilos[i]);
            estilosBold[i].cloneStyleFrom(estilos[i]);
            estilosBold[i].setFont(fontBold);
        }

        int numFila = 1;
        for (ProductoEntity p : productos) {
            XSSFRow fila = hoja.createRow(numFila);
            // 1 is green for odd ones, 0 is white for even ones
            int colorIdx = (numFila % 2 != 0) ? 1 : 0;

            // We put the data in an array to avoid the creation of all the cells by hand
            Object[] datos = {
                    p.getProducto().getCodigo(), p.getProducto().getMarca(),
                    p.getProducto().getModelo(), p.getProducto().getCategoria(),
                    p.getPrecioFinal().doubleValue(), p.getCost().doubleValue(), p.getProfit().doubleValue()
            };

            for (int i = 0; i < datos.length; i++) {
                XSSFCell celda = fila.createCell(i);
                if (datos[i] instanceof String) celda.setCellValue((String) datos[i]);
                else if (datos[i] instanceof Double) {
                    celda.setCellValue((Double) datos[i]);
                }

                celda.setCellStyle(i == 0 ? estilosBold[colorIdx] : estilos[colorIdx]);
            }
            numFila++;
        }

        for (int i = 0; i < 7; i++) hoja.autoSizeColumn(i);
    }

    /**
     * This method applies the green border on the table.
     * @param estilo this parameter is to set the style.
     */
    private void applyGreenBorder(XSSFCellStyle estilo) {
        // setting up the style from every border
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setTopBorderColor(IndexedColors.DARK_GREEN.getIndex());
        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBottomBorderColor(IndexedColors.DARK_GREEN.getIndex());
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setLeftBorderColor(IndexedColors.DARK_GREEN.getIndex());
        estilo.setBorderRight(BorderStyle.THIN);
        estilo.setRightBorderColor(IndexedColors.DARK_GREEN.getIndex());
    }

    /**
     * Do the validation and creates the directory if it doesn't exists and save it.
     *
     * @param libro is the Excel book with all the data.
     * @param path  is the destination directory.
     * @throws IOException this eror is for the operations of input and output.
     */
    private void saveExcelFile(XSSFWorkbook libro, String path, String fileName) throws IOException {
        File carpeta = new File(path);
        if (!carpeta.exists()){
            carpeta.mkdirs();
        }

        try (FileOutputStream salida = new FileOutputStream(new File(carpeta, fileName))) {
            libro.write(salida);
        }
        // try-with-resources close automatic the outputStream
        libro.close();
    }

}
