package com.example.casemng.constant;

import lombok.Getter;

public class Constant {

	@Getter
	public static enum ShippingStatus{
		
		AwaitingPayment(1, "支払待ち"),
		AwaitingShipment(2, "発送待ち"),
		Shipped(3, "発送済み"),
		Cancel(4, "キャンセル")
		;
		
		private ShippingStatus(int value, String view) {
            this.value = value;
            this.view = view;
        }

        private int value;
        private String view;
	}
	
	@Getter
	public static enum OrderStatus{
		
		Order("受注"),
		Quantity("見積"),
		Inquiry("問い合わせ")
		;
		
		private OrderStatus(String view) {
            this.view = view;
        }

        private String view;
	}
}
