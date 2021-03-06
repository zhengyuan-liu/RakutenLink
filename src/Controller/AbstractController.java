package Controller;

import Model.AbstractModel;
import View.AbstractViewPanel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by ChenLetian on 4/1/16.
 * 抽象Controller类
 */
public class AbstractController implements PropertyChangeListener {

    protected ArrayList<AbstractViewPanel> registeredViews;
    protected ArrayList<AbstractModel> registeredModels;

    public AbstractController() {
        registeredViews = new ArrayList<>();
        registeredModels = new ArrayList<>();
    }

    public void addModel(AbstractModel model) {
        registeredModels.add(model);
        model.addPropertyChangeListener(this);
    }

    public void removeModel(AbstractModel model) {
        registeredModels.remove(model);
        model.removePropertyChangeListener(this);
    }

    public void addView(AbstractViewPanel view) {
        registeredViews.add(view);
    }

    public void removeView(AbstractViewPanel view) {
        registeredViews.remove(view);
    }

    // This method is to let all bound views know the change of model
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue = An object that represents the new value
     * of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {

        for (AbstractModel model: registeredModels) {
            try {
                Method method = model.getClass().getMethod("set"+propertyName, newValue.getClass());
                method.invoke(model, newValue);
            }
            catch (Exception ex) {
                //  Handle exception.
            }
        }
    }

}
