package io.github.winsontse.hearteyes.page.todo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import io.github.winsontse.hearteyes.app.AppComponent;
import io.github.winsontse.hearteyes.page.base.BasePresenter;
import io.github.winsontse.hearteyes.page.todo.component.DaggerTodoListComponent;
import io.github.winsontse.hearteyes.page.todo.contract.TodoListContract;
import io.github.winsontse.hearteyes.page.todo.module.TodoListModule;
import io.github.winsontse.hearteyes.page.todo.presenter.TodoListPresenter;
import io.github.winsontse.hearteyes.page.base.BaseFragment;
import io.github.winsontse.hearteyes.R;

public class TodoListFragment extends BaseFragment implements TodoListContract.View {

    @Inject
    TodoListPresenter presenter;

    public static TodoListFragment newInstance() {
        Bundle args = new Bundle();
        TodoListFragment fragment = new TodoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todo_list, container, false);
        return rootView;
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
    protected BasePresenter getPresenter() {
        return presenter;
    }

}