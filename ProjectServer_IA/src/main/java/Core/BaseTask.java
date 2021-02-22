package Core;

import javafx.concurrent.Task;

public abstract class BaseTask<T> extends Task<T> {
  protected String _taskName;

  public BaseTask(String taskName){
    _taskName = taskName;
  }

  @Override
  protected T call() throws Exception {
    T result = null;
    try{
      System.out.println("Running task: " + _taskName);
      result = runTask();
      System.out.println("Task: " + _taskName + " done");
    }
    catch (Exception err) {
      System.out.println("Running task: " + _taskName + " Error: " + err.getMessage());
    }
    return result;
  }

  protected abstract T runTask();
}
