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
      *First, we write the file to disk via productoDAO.writeFile.
       * Then, we read its generated length using file.length().
        *Finally, we update the fileSize field and rewrite the file with the exact byte size.


