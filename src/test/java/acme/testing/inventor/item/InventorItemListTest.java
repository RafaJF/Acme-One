package acme.testing.inventor.item;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class InventorItemListTest extends TestHarness{
	
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/item/list-mine-components.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveListComponentTest(final int recordIndex,final String name, final String code,final String technology,
		final String description, final String retailPrice, final String info) {
		
		super.signIn("inventor1", "inventor1");
		super.clickOnMenu("Inventor", "List my components");
		super.checkListingExists();
		super.sortListing(0, "asc");
		
		super.checkColumnHasValue(recordIndex, 0, name);
		super.checkColumnHasValue(recordIndex, 1, code);
		super.checkColumnHasValue(recordIndex, 2, technology);
		super.checkColumnHasValue(recordIndex, 3, description);
		super.checkColumnHasValue(recordIndex, 4, retailPrice);
		super.checkColumnHasValue(recordIndex, 5, info);
		
		super.signOut();
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/inventor/item/list-mine-tools.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveListToolTest(final int recordIndex,final String name, final String code,final String technology,
		final String description, final String retailPrice, final String info) {
		
		super.signIn("inventor1", "inventor1");
		super.clickOnMenu("Inventor", "List my tools");
		super.checkListingExists();
		super.sortListing(0, "asc");
		
		super.checkColumnHasValue(recordIndex, 0, name);
		super.checkColumnHasValue(recordIndex, 1, code);
		super.checkColumnHasValue(recordIndex, 2, technology);
		super.checkColumnHasValue(recordIndex, 3, description);
		super.checkColumnHasValue(recordIndex, 4, retailPrice);
		super.checkColumnHasValue(recordIndex, 5, info);
		
		super.signOut();
	}

}
