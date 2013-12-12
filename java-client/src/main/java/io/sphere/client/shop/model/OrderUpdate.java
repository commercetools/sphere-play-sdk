package io.sphere.client.shop.model;

import io.sphere.internal.command.CartCommands;
import io.sphere.internal.command.OrderCommands;
import io.sphere.internal.command.Update;
import io.sphere.internal.command.UpdateAction;

import java.util.List;

public class OrderUpdate extends Update<UpdateAction> {
    public OrderUpdate setPaymentState(PaymentState paymentState) {
        add(new OrderCommands.UpdatePaymentState(paymentState));
        return this;
    }

    public OrderUpdate setShipmentState(ShipmentState shipmentState) {
        add(new OrderCommands.UpdateShipmentState(shipmentState));
        return this;
    }

    /** Sets the tracking data, some info about the delivery (like a DHL tracking number) which is useful to keep an eye
     * on your delivery, view its status etc.*/
    public OrderUpdate addTrackingData(final TrackingData ... trackingData) {
        for (final TrackingData item : trackingData) {
            add(new CartCommands.SetTrackingData(item));
        }
        return this;
    }

    /** Sets the tracking data, some info about the delivery (like a DHL tracking number) which is useful to keep an eye
     * on your delivery, view its status etc.*/
    public OrderUpdate addTrackingData(final List<TrackingData> trackingData) {
        for (final TrackingData item : trackingData) {
            add(new CartCommands.SetTrackingData(item));
        }
        return this;
    }
}
