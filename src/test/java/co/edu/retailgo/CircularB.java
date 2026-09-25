package co.edu.retailgo;

import co.edu.retailgo.minispring.RGComponent;
import co.edu.retailgo.minispring.RGInject;

@RGComponent
public class CircularB {
    @RGInject
    public CircularB(CircularA ignored) { }
}
