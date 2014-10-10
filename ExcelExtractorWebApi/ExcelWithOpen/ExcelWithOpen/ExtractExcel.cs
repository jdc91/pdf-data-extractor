﻿using ClosedXML.Excel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ExcelWithOpen
{
    class ExtractExcel
    {
        private static void exTest()
        {
            List<String> categories;
            List<String> companies;
            ExtractCategoriesCompanies("NorthwindData.xlsx", out categories, out companies);

            // Do something with the categories and companies
        }

        private static void ExtractCategoriesCompanies(string northwinddataXlsx, out List<string> categories, out List<string> companies)
        {
            categories = new List<string>();
            const int coCategoryId = 1;
            const int coCategoryName = 2;

            var wb = new XLWorkbook(northwinddataXlsx);
            var ws = wb.Worksheet("Data");

            // Look for the first row used
            var firstRowUsed = ws.FirstRowUsed();

            // Narrow down the row so that it only includes the used part
            var categoryRow = firstRowUsed.RowUsed();

            // Move to the next row (it now has the titles)
            categoryRow = categoryRow.RowBelow();

            // Get all categories
            while (!categoryRow.Cell(coCategoryId).IsEmpty())
            {
                String categoryName = categoryRow.Cell(coCategoryName).GetString();
                categories.Add(categoryName);

                categoryRow = categoryRow.RowBelow();
            }

            // There are many ways to get the company table.
            // Here we're using a straightforward method.
            // Another way would be to find the first row in the company table
            // by looping while row.IsEmpty()

            // First possible address of the company table:
            var firstPossibleAddress = ws.Row(categoryRow.RowNumber()).FirstCell().Address;
            // Last possible address of the company table:
            var lastPossibleAddress = ws.LastCellUsed().Address;

            // Get a range with the remainder of the worksheet data (the range used)
            var companyRange = ws.Range(firstPossibleAddress, lastPossibleAddress).RangeUsed();

            // Treat the range as a table (to be able to use the column names)
            var companyTable = companyRange.AsTable();

            // Get the list of company names
            companies = companyTable.DataRange.Rows()
                .Select(companyRow => companyRow.Field("Company Name").GetString())
                .ToList();
        }
    }
}
