# ad-practica-ra1

Victor and Ivan first practice in the assignature of Data Access

Exercise 1: Read the XML and do the calculations.

We read the 'inventario_junio2025.xml' and calculated the final price with discount, total cost and profit for each product

1. Then we use JAXB in ProductoDAOImpl.java to read the XML file.
2. In ProductoService.java we create a method called readFile. This method takes each product and use     BigDecimal to do calculationn:

     * Final price: Substract the discount percentage from the original price.
     * Cost: Add storages costs + shipping costs.
     * Profit: Subtract the total cost from the final price.
  
Exercise 2: Create the TXT summary file.

Create a .txt file named result_junio2025.txt int he src/main/resources/export
foulder. This file must contain: the date, the number of products, the total profit, the file paths and the size in bytes.

1. In ProductoService.java, the exportSummary method sums up the total profit across all products.
2. Created a helper method buildNameTxt to convert the input filename from .xml to .txt and ensure the target directory exists.
3. To calculate the exact size of the resulting .txt file:
     * First, we write the file to disk via productoDAO.writeFile.
     * Then, we read its generated length using file.length().
     * Finally, we update the fileSize field and rewrite the file with the exact byte size.

Exercise 3: Excel Export with Apache POI via DAO Delegation

Generate an `.xlsx` Excel file in the `src/main/resources/export` folder populated with the processed inventory data.

1. Service Layer Coordination (ProductoService.java):
   * The `exportExcel(path, fileXml) method reuses the readFile(fileXml) method from Exercise 1 to retrieve the list of products with their already calculated costs and profits (`List<ProductoEntity>`).
   * It delegates the responsibility of creating and saving the Excel file to the DAO by calling `productoDAO.exportExcelDAO(path, productos).
  
2. DAO Layer File Creation & Styling (ProductoDAOImpl.java):
   We implemented the 'exportExcelDAO' method using private helper methods to keep the code clean and maintainable:
   * Workbook & Sheet Initialization: Created an instance of 'XSSFWorkbook' and a sheet ('XSSFSheet') named "ResumenInventario".
   * Styled Headers ('createHeaderRow'): Created the first row with column names ("Código", "Marca", "Modelo", "Categoría", "Precio Final", "Coste", "Beneficio"). Applied bold text, center alignment, and dark green borders.
   * Data Population & Zebra Striping ('fillDataRow'):
     * Iterated through the list of 'ProductoEntity' items and set cell values according to their data type.
     * Applied a zebra pattern: odd rows feature a light green background, while even rows stay white.
     * Formatted the first column ("Código") in bold text for emphasis.
     * Called 'autoSizeColumn' across all 7 columns so cell widths adjust automatically to fit the content.
