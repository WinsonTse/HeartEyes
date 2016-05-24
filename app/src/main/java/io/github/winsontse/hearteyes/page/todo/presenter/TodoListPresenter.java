package io.github.winsontse.hearteyes.page.todo.presenter;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.page.todo.contract.TodoListContract;
import io.github.winsontse.hearteyes.page.base.BasePresenterImpl;

public class TodoListPresenter extends BasePresenterImpl implements TodoListContract.Presenter {
    private TodoListContract.View view;

    @Inject
    public TodoListPresenter(TodoListContract.View view) {
        this.view = view;
    }
}
