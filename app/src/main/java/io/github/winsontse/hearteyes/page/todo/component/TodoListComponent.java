package io.github.winsontse.hearteyes.page.todo.component;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.todo.TodoListFragment;
import io.github.winsontse.hearteyes.page.todo.module.TodoListModule;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = {TodoListModule.class})
public interface TodoListComponent {

    void inject(TodoListFragment fragment);

}

