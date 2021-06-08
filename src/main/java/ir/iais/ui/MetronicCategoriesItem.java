package ir.iais.ui;

/**
 * @author vahid
 * create on 5/30/2021
 */
public class MetronicCategoriesItem extends ProtectedPanel {

    private final String categoryId;

    public MetronicCategoriesItem(String categoryId) {
        super("categoryItem");
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }
}
