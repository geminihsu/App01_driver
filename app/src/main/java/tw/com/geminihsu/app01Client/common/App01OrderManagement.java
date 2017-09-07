package tw.com.geminihsu.app01Client.common;

import java.util.ArrayList;
import java.util.List;

import tw.com.geminihsu.app01Client.bean.NormalOrder;


public class App01OrderManagement {
	private static final String TAG = App01OrderManagement.class.getSimpleName();
	/*避免null pointer exception,所以先new好*/
	private static List<NormalOrder>  orders = new ArrayList<NormalOrder>();

	// 設定deviceList
	public static void setOrderList(List<NormalOrder> orderList) {
		orders = orderList;

		// 準備建立deviceId

		if (orders != null && orders.size() != 0) {
			for (int i = 0; i < orders.size(); i++) {
				NormalOrder orderInfoBean = orderList.get(i);

			}
		}
	}	


	
	
	//針對index remove list
	public static  NormalOrder removeDeviceByIndex(int index)
	{
		//MyDeviceInfoBean myDeviceInfoBean = deviceList.remove(index);

		NormalOrder myDeviceInfoBean = orders.get(index);
		//真正刪除
		orders.remove(index);
		
		return myDeviceInfoBean;
		
	
	}
	//clear DeviceList
	public static  void clearDeviceList()
	{

		
		//清除list
		orders.clear();
		
	}


}
