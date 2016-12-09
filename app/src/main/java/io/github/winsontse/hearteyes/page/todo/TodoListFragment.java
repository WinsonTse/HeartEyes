package io.github.winsontse.hearteyes.page.todo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.R;
import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.todo.component.DaggerTodoListComponent;
import io.github.winsontse.hearteyes.page.todo.contract.TodoListContract;
import io.github.winsontse.hearteyes.page.todo.module.TodoListModule;
import io.github.winsontse.hearteyes.page.todo.presenter.TodoListPresenter;

public class TodoListFragment extends BaseFragment implements TodoListContract.View {

    @Inject
    TodoListPresenter presenter;

    public static TodoListFragment newInstance() {
        Bundle args = new Bundle();
        TodoListFragment fragment = new TodoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView( @Nullable Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_todo_list;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerTodoListComponent.builder()
                .appComponent(appComponent)
                .todoListModule(new TodoListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

}