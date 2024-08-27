package com.boii.bankapp.service.impl;

import com.boii.bankapp.dto.EmailDetails;
import com.boii.bankapp.entity.Transaction;
import com.boii.bankapp.entity.User;
import com.boii.bankapp.repository.TransactionRepository;
import com.boii.bankapp.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private static final String FILE_PATH = "D:\\CodX\\SpringBoot\\BankAppYT\\BankStatements\\myStatement.pdf";
    private static final String BANK_NAME = "Bank_App (India)";
    private static final String BANK_ADDRESS = "128, Some Address, India";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException {
        LocalDate start = LocalDate.parse(startDate, DATE_FORMATTER);
        LocalDate end = LocalDate.parse(endDate, DATE_FORMATTER);

        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt().isAfter(end))
                .toList();

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = Optional.ofNullable(user)
                .map(u -> String.join(" ", u.getFirstName(), u.getMiddleName(), u.getLastName()))
                .orElse("Unknown User");

        try {
            generatePdf(transactionList, user, startDate, endDate, customerName);
        } catch (IOException e) {
            log.error("Error generating PDF file", e);
            throw new DocumentException("Failed to generate PDF", e);
        }

        sendEmailWithAttachment(user);

        return transactionList;
    }

    private void generatePdf(List<Transaction> transactions, User user, String startDate, String endDate, String customerName) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addBankInfo(document);
            addStatementInfo(document, startDate, endDate, customerName, user.getAddress(), user.getEmail(), user.getAccountNumber());
            addTransactionTable(document, transactions);

            document.close();
        }
        log.info("PDF generated successfully at {}", FILE_PATH);
    }

    private void addBankInfo(Document document) throws DocumentException {
        PdfPTable bankInfoTable = new PdfPTable(1);
        bankInfoTable.setWidthPercentage(100);


        // Creating the bank name cell with blue color and specific font size
        Font bankNameFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE);
        Phrase bankNamePhrase = new Phrase("Bank_App ", bankNameFont);

        // Adding "(India)" with a different style (italic and black)
        Font countryFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, BaseColor.BLACK);
        bankNamePhrase.add(new Chunk("(India)", countryFont));

        PdfPCell bankNameCell = new PdfPCell(bankNamePhrase);
        bankNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bankNameCell.setBorder(Rectangle.NO_BORDER); // Removing the border

        // Creating the address cell
        Font addressFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
        PdfPCell addressCell = createCell("Branch Address: 128, Some Address, India", Element.ALIGN_LEFT, BaseColor.WHITE, addressFont);
        addressCell.setBorder(Rectangle.NO_BORDER); // Removing the border

        // Adding cells to the table
        bankInfoTable.addCell(bankNameCell);
        bankInfoTable.addCell(addressCell);

        // Adding the table to the document
        document.add(bankInfoTable);
    }

    private void addStatementInfo(Document document, String startDate, String endDate, String customerName, String address, String email, String accountNumber) throws DocumentException {
        PdfPTable statementInfoTable = new PdfPTable(2);
        statementInfoTable.setWidthPercentage(100);
        statementInfoTable.setWidths(new int[]{50, 50});

        // Left Column: Bank and Statement Dates
        PdfPCell leftColumn = new PdfPCell();
        leftColumn.setBorder(Rectangle.NO_BORDER);
        leftColumn.addElement(new Phrase("Statement Dates:", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        leftColumn.addElement(new Phrase("Start Date: " + startDate, new Font(Font.FontFamily.HELVETICA, 8)));
        leftColumn.addElement(new Phrase("End Date: " + endDate, new Font(Font.FontFamily.HELVETICA, 8)));

        // Right Column: Customer Info
        PdfPCell rightColumn = new PdfPCell();
        rightColumn.setBorder(Rectangle.NO_BORDER);
        rightColumn.addElement(new Phrase("Customer Info", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
        rightColumn.addElement(new Phrase("Customer Name: " + customerName, new Font(Font.FontFamily.HELVETICA, 8)));
        rightColumn.addElement(new Phrase("Address: " + address, new Font(Font.FontFamily.HELVETICA, 8)));
        rightColumn.addElement(new Phrase("Email: " + email, new Font(Font.FontFamily.HELVETICA, 8)));
        rightColumn.addElement(new Phrase("Account Number: " + accountNumber, new Font(Font.FontFamily.HELVETICA, 8)));

        statementInfoTable.addCell(leftColumn);
        statementInfoTable.addCell(rightColumn);

        document.add(statementInfoTable);
    }

    private void addTransactionTable(Document document, List<Transaction> transactions) throws DocumentException {
        PdfPTable transactionTable = new PdfPTable(4);
        transactionTable.setWidthPercentage(100);
        transactionTable.setSpacingBefore(10f);
        transactionTable.setSpacingAfter(10f);
        transactionTable.setWidths(new int[]{25, 25, 25, 25});

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
        transactionTable.addCell(createCell("DATE", Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY, headerFont));
        transactionTable.addCell(createCell("TRANSACTION TYPE", Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY, headerFont));
        transactionTable.addCell(createCell("TRANSACTION AMOUNT", Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY, headerFont));
        transactionTable.addCell(createCell("TRANSACTION STATUS", Element.ALIGN_CENTER, BaseColor.LIGHT_GRAY, headerFont));

        Font cellFont = new Font(Font.FontFamily.HELVETICA, 8);
        transactions.forEach(transaction -> {
            transactionTable.addCell(createCell(transaction.getCreatedAt().toString(), Element.ALIGN_CENTER, BaseColor.WHITE, cellFont));
            transactionTable.addCell(createCell(transaction.getTransactionType(), Element.ALIGN_CENTER, BaseColor.WHITE, cellFont));
            transactionTable.addCell(createCell(transaction.getAmount().toString(), Element.ALIGN_CENTER, BaseColor.WHITE, cellFont));
            transactionTable.addCell(createCell(transaction.getStatus(), Element.ALIGN_CENTER, BaseColor.WHITE, cellFont));
        });

        document.add(transactionTable);
    }

    private PdfPCell createCell(String content, int alignment, BaseColor backgroundColor, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBackgroundColor(backgroundColor);
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5f);
        return cell;
    }

    private void sendEmailWithAttachment(User user) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipients(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Your transaction statements for the requested dates are attached.")
                .attachment(FILE_PATH)
                .build();

        emailService.senderEmailAlert(emailDetails);
        log.info("Email sent to {}", user.getEmail());
    }
}
