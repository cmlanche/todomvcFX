package todomvcfx.mvvmfx.ui.additems;

import de.saxsys.mvvmfx.testingutils.FxTestingUtils;
import de.saxsys.mvvmfx.testingutils.jfxrunner.JfxRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import todomvcfx.mvvmfx.model.TodoItem;
import todomvcfx.mvvmfx.model.TodoItemStore;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JfxRunner.class)
public class AddItemsViewModelTest {


    private AddItemsViewModel viewModel;

    private TodoItem item1;
    private TodoItem item2;
    private TodoItem item3;

    private TodoItemStore itemStore;

    @Before
    public void setup() {
        itemStore = new TodoItemStore();
        viewModel = new AddItemsViewModel(itemStore);

        item1 = new TodoItem("1");
        item2 = new TodoItem("2");
        item3 = new TodoItem("3");

        itemStore.getItems().clear();
    }

    @Test
    public void testAddNewItem() {
        // given
        assertThat(itemStore.getItems()).isEmpty();

        // when
        viewModel.newItemValueProperty().set("test");
        viewModel.addItem();

        // then
        assertThat(itemStore.getItems()).hasSize(1);
        final TodoItem item = itemStore.getItems().get(0);
        assertThat(item.getText()).isEqualTo("test");

        assertThat(viewModel.newItemValueProperty().get()).isEmpty();
    }

    @Test
    public void testNotAddItemWithEmptyText() {
        // given
        assertThat(itemStore.getItems()).isEmpty();

        // when
        viewModel.newItemValueProperty().set("");
        viewModel.addItem();

        // then
        assertThat(itemStore.getItems()).isEmpty();


        // when
        viewModel.newItemValueProperty().set("   ");
        viewModel.addItem();

        // then
        assertThat(itemStore.getItems()).isEmpty();
    }

    @Test
    public void testSelectAllIsClicked() {
        // given

        itemStore.getItems().add(item1);
        itemStore.getItems().add(item2);
        itemStore.getItems().add(item3);

        assertThat(viewModel.allSelectedProperty().get()).isFalse();

        assertThat(itemStore.getItems().stream().allMatch(item -> !item.isCompleted())).isTrue();


        // when
        viewModel.allSelectedProperty().setValue(true);
        viewModel.selectAll();
        FxTestingUtils.waitForUiThread();

        // then
        assertThat(viewModel.allSelectedProperty().get()).isTrue();
        assertThat(itemStore.getItems().stream().allMatch(item -> item.isCompleted())).isTrue();
    }

    @Test
    public void testSelectAllGetsCheckedWhenAllItemsAreCompleted() {
        // given

        itemStore.getItems().add(item1);
        itemStore.getItems().add(item2);
        itemStore.getItems().add(item3);

        item1.setCompleted(true);
        item2.setCompleted(true);

        assertThat(viewModel.allSelectedProperty().get()).isFalse();


        // when
        item3.setCompleted(true);
        FxTestingUtils.waitForUiThread();

        // then
        assertThat(viewModel.allSelectedProperty().get()).isTrue();
    }

    @Test
    public void testSelectAllGetsUncheckedWhenAnItemAreUncompleted() {
        itemStore.getItems().add(item1);
        itemStore.getItems().add(item2);
        itemStore.getItems().add(item3);

        item1.setCompleted(true);
        item2.setCompleted(true);
        item3.setCompleted(true);

        FxTestingUtils.waitForUiThread();
        assertThat(viewModel.allSelectedProperty().get()).isTrue();

        // when
        item1.setCompleted(false);
        FxTestingUtils.waitForUiThread();


        // then
        assertThat(viewModel.allSelectedProperty().get()).isFalse();
    }

}
