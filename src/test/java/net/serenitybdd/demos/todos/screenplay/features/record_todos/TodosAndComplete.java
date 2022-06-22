package net.serenitybdd.demos.todos.screenplay.features.record_todos;
import net.serenitybdd.demos.todos.screenplay.questions.TheItemStatus;
import net.serenitybdd.demos.todos.screenplay.questions.TheItems;
import net.serenitybdd.demos.todos.screenplay.tasks.AddATodoItem;
import net.serenitybdd.demos.todos.screenplay.tasks.Clear;
import net.serenitybdd.demos.todos.screenplay.tasks.CompleteItem;
import net.serenitybdd.demos.todos.screenplay.tasks.Start;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import static java.util.Collections.EMPTY_LIST;
import static net.serenitybdd.demos.todos.screenplay.model.TodoStatus.Completed;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

/**
 * This example illustrates using Serenity Steps with JUnit.
 */
@RunWith(SerenityRunner.class)
@WithTags({
        @WithTag("Screenplay pattern"),
        @WithTag("version:RELEASE-1"),
})
public class TodosAndComplete {

    private Actor james = Actor.named("James");
    @Managed private WebDriver hisBrowser;
    @Before public void jamesCanBrowseTheWeb() {
        james.can(BrowseTheWeb.with(hisBrowser));
    }

    @Test
    public void should_be_able_to_add_the_first_todo_item() {

        givenThat(james).wasAbleTo(Start.withAnEmptyTodoList());

        when(james).attemptsTo(AddATodoItem.called("Read"));

        then(james).should(seeThat(TheItems.displayed(), hasItem("Read")));
    }

    @Test
    public void should_be_able_to_add_additional_todo_items() {

        givenThat(james).wasAbleTo(Start.withATodoListContaining("Read"));

        when(james).attemptsTo(AddATodoItem.called("Write"));

        then(james).should(seeThat(TheItems.displayed(),
                                   hasItems("Read", "Car")));

    }
    @Test
    public void should_be_able_to_empty_completed_items() {

        givenThat(james).wasAbleTo(Start.withATodoListContaining("Read","Write"));

        when(james).wasAbleTo(Start.openCompletedList());

        then(james).should(seeThat(TheItems.displayed(), equalTo(EMPTY_LIST)));

    }


    @Test
    public void should_be_able_to_complete_a_todo() {

        givenThat(james).wasAbleTo(
                Start.withATodoListContaining("Read", "Write")
        );

        when(james).attemptsTo(
                CompleteItem.called("Read")
        );

        then(james).should(
                seeThat(TheItemStatus.forTheItemCalled("Read"), is(Completed)),
                seeThat(TheItems.leftCount(), is(1))
        );
        when(james).wasAbleTo(Start.openCompletedList());

        then(james).should(seeThat(TheItems.displayed(),
                hasItems("Read")));

    }


    @Test
    public void should_be_able_to_clear_completed_todos() {

        givenThat(james).wasAbleTo(Start.withATodoListContaining("Read", "Write"));

        when(james).attemptsTo(
                CompleteItem.called("Read"),
                Clear.completedItems()
        );

        then(james).should(seeThat(TheItems.displayed(), contains("Write")));

        then(james).should(seeThat(TheItems.leftCount(), is(1)));
    }


}