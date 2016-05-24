package io.github.winsontse.hearteyes.page.todo.module;

import dagger.Module;
import dagger.Provides;
import io.github.winsontse.hearteyes.page.todo.contract.TodoListContract;
import io.github.winsontse.hearteyes.util.scope.FragmentScope;


@Module
public class TodoListModule {
    private TodoListContract.View view;

    public TodoListModule(TodoListContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    TodoListContract.View provideTodoListView() {
        return view;
    }
}
