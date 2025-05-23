package MyCooking.com;

import MyCooking.com.models.Invoice;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BillingSystemTest {

    private BillingSystem billingSystem;

    @Before
    public void setUp() {
        billingSystem = new BillingSystem();
    }

    @Test
    public void testAdminLoginCorrectPassword() {
        
        billingSystem.adminPassword = "admin123"; 
        assertTrue(billingSystem.adminLogin("admin123"));
        assertTrue(billingSystem.isAdminLoggedIn());
    }

    @Test
    public void testAdminLoginIncorrectPassword() {
        billingSystem.adminPassword = "admin123";
        assertFalse(billingSystem.adminLogin("wrong"));
        assertFalse(billingSystem.isAdminLoggedIn());
    }

    @Test
    public void testAdminLoginNullPassword() {
        assertFalse(billingSystem.adminLogin(null));
        assertFalse(billingSystem.isAdminLoggedIn());
    }

    @Test
    public void testAdminLogout() {
        billingSystem.adminPassword = "admin123";
        billingSystem.adminLogin("admin123");
        billingSystem.adminLogout();
        assertFalse(billingSystem.isAdminLoggedIn());
    }

    @Test
    public void testCompleteOrderValid() {
        assertTrue(billingSystem.completeOrder("customer1", 50.0));
        List<Invoice> invoices = billingSystem.getInvoices();
        assertFalse(invoices.isEmpty());
        assertEquals("customer1", invoices.get(0).getCustomerId());
        assertEquals(50.0, invoices.get(0).getAmount(), 0.01);
    }

    @Test
    public void testCompleteOrderInvalidCustomer() {
        assertFalse(billingSystem.completeOrder(null, 50.0));
        assertFalse(billingSystem.completeOrder("", 50.0));
    }

    @Test
    public void testCompleteOrderWithDefaultAmount() {
        assertTrue(billingSystem.completeOrder("customerDefault"));
        List<Invoice> invoices = billingSystem.getInvoices();
        assertFalse(invoices.isEmpty());
        assertEquals(100.0, invoices.get(0).getAmount(), 0.01);
    }

    @Test
    public void testGenerateAndSendInvoiceValid() {
        assertTrue(billingSystem.generateAndSendInvoice("customer2", 30.0));
        List<Invoice> invoices = billingSystem.getInvoices();
        assertTrue(invoices.stream().anyMatch(inv -> inv.getCustomerId().equals("customer2")));
    }

    @Test
    public void testGenerateAndSendInvoiceInvalidCustomer() {
        assertFalse(billingSystem.generateAndSendInvoice("", 30.0));
        assertFalse(billingSystem.generateAndSendInvoice(null, 30.0));
    }

    @Test
    public void testGenerateAndSendInvoiceDefaultAmount() {
        assertTrue(billingSystem.generateAndSendInvoice("customerDefault"));
    }

    @Test
    public void testFinalizeOrderWithInvoices() {
        billingSystem.completeOrder("c1", 10);
        billingSystem.completeOrder("c2", 20);
        billingSystem.finalizeOrder(); 
        assertFalse(billingSystem.getInvoices().isEmpty());
    }

    @Test
    public void testFinalizeOrderNoInvoices() {
        billingSystem.clearInvoices();
        billingSystem.finalizeOrder(); 
        assertTrue(billingSystem.getInvoices().isEmpty());
    }

    @Test
    public void testDisplayFinancialReportWithAdmin() {
        billingSystem.adminPassword = "admin123";
        billingSystem.adminLogin("admin123");
        billingSystem.completeOrder("c1", 100);
        billingSystem.displayFinancialReport();
    }

    @Test
    public void testDisplayFinancialReportWithoutAdmin() {
        billingSystem.clearInvoices();
        billingSystem.adminLogout();
        billingSystem.displayFinancialReport();
    }

    @Test
    public void testClearInvoices() {
        billingSystem.completeOrder("c1", 10);
        billingSystem.clearInvoices();
        assertTrue(billingSystem.getInvoices().isEmpty());
    }
}
