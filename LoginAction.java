package com.internousdev.spring.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.spring.dao.CartInfoDAO;
import com.internousdev.spring.dao.UserInfoDAO;
import com.internousdev.spring.dto.CartInfoDTO;
import com.internousdev.spring.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;


public class LoginAction extends ActionSupport implements SessionAware{

	private String userId;
	private String password;
	private Map<String, Object> session;
	private UserInfoDAO userInfoDAO = new UserInfoDAO();
	private String errorMessage;
	private List<String> errorMessageList1;
	private List<String> errorMessageList2;
	private boolean savedUserIdFlg;
	private int productId;
	private int productCount;

	private List<CartInfoDTO> productInfoListinCart = new ArrayList<CartInfoDTO>();
	private int cartTotalPrice = 0;
	private String emptyCartMessage = null;

	public String execute() throws SQLException {
		String result = ERROR;

		session.remove("savedUserIdFlg");

		if(session.containsKey("createUserFlg") && Integer.parseInt(session.get("createUserFlg").toString()) == 1) {
			//ユーザー入力完了画面から遷移場合
			userId = session.get("userIdForCreateUser").toString();
			//ユーザー情報入力完了画面から遷移した場合にユーザー情報がsessionに入っているため削除
			session.remove("userIdForCreateUser");
			session.remove("createUserFlg");

		} else {
			InputChecker inputChecker = new InputChecker();
			errorMessageList1 = inputChecker.doCheck("ユーザーID", userId, 1, 8, true,false,false,true,false,false);
			errorMessageList2 = inputChecker.doCheck("パスワード",password,1,16,true,false,false,true,false,false);
			if(errorMessageList1.size() > 0 || errorMessageList2.size() >0){
				session.put("loginFlg",0);

				//入力値エラーあり
				return result;
			}
			//ログイン認証
			//jspから送られてきた、userIdとpasswordがDBと一致するか確認
			if(!userInfoDAO.isExistsUserInfo(userId,password)) {
				setErrorMessage("ユーザーIDまたはパスワードが異なります。");
				return result;
			}

		}

		// セッションタイムアウト
		if(!session.containsKey("tempUserId")) {
			return "session Timeout";
		}


		//仮ユーザーIDでのカート情報の有無の確認
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		String tempUserId = session.get("tempUserId").toString();
		List<CartInfoDTO> cartInfoListbyTempUserId = cartInfoDAO.getCartInfo(tempUserId);
		if(cartInfoListbyTempUserId != null && cartInfoListbyTempUserId.size() > 0) {
			// 仮ユーザーIDでのカート情報が存在する場合
			int res = 0;
			// DBのカート情報テーブル内で「ユーザーID」に紐づく同じ商品IDのカート情報が存在するかの確認
			for(CartInfoDTO cartInfo: cartInfoListbyTempUserId) {
				boolean checkCartInfoByUserId = false;
				checkCartInfoByUserId = cartInfoDAO.isCartInfoExistsByUserIdandProductId(userId, cartInfo.getProductId());
				if(checkCartInfoByUserId == true) {
					// DBのカート情報テーブル内で「ユーザーID」に紐づく同じ商品IDのカート情報が存在する場合
					res += cartInfoDAO.addCartByLogin(userId, tempUserId, cartInfo.getProductId(), cartInfo.getProductCount());
					cartInfoDAO.deleteCartItem(tempUserId, cartInfo.getProductId());
				} else {
					// DBのカート情報テーブル内で「ユーザーID」に紐づく同じ商品IDのカート情報が存在しない場合
					res += cartInfoDAO.updateCartUserId(userId, tempUserId, cartInfo.getProductId());
				}
			}
			if(res != cartInfoListbyTempUserId.size()) {
				return "DBerror";
			}
		}

		//ユーザー情報をsessionに登録し、tempedUserIdを削除
		session.put("userId", userId);
		session.put("loginFlg", 1);

		if(savedUserIdFlg) {
			session.put("savedUserIdFlg", true);
		}
		session.remove("tempUserId");

		if(session.containsKey("cartFlg") && Integer.parseInt(session.get("cartFlg").toString()) == 1) {
			session.remove("cartFlg");
			productInfoListinCart = cartInfoDAO.getProductInfoinCart(userId);
			if(productInfoListinCart.size() > 0) {
				// カート情報が存在した場合、カート画面にカート情報を表示させるための処理
				result = "cart";
				for(CartInfoDTO s: productInfoListinCart) {
					cartTotalPrice += s.getPrice() * s.getProductCount();
				}
			} else {
				// カート情報が存在しない場合
				result = "cart";
				setEmptyCartMessage("カート情報がありません。");
			}
		} else {
			result = SUCCESS;
		}
		return result;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId =  userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public boolean getSavedUserIdFlg() {
		return savedUserIdFlg;
	}
	public void setSavedUserIdFlg(boolean savedUserIdFlg) {
		this.savedUserIdFlg = savedUserIdFlg;
	}
	public Map<String,Object> getSession(){
		return this.session;
	}
	@Override
	public void setSession(Map<String,Object> session) {
		this.session = session;
	}
	public int getProductedId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getProductCoint() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	public List<String> getErrorMessageList1() {
		return errorMessageList1;
	}

	public void setErrorMessageList1(List<String> errorMessageList1) {
		this.errorMessageList1 = errorMessageList1;
	}

	public List<String> getErrorMessageList2() {
		return errorMessageList2;
	}

	public void setErrorMessageList2(List<String> errorMessageList2) {
		this.errorMessageList2 = errorMessageList2;
	}


	// 以下、カート画面への遷移用
	public List<CartInfoDTO> getProductInfoListinCart() {
		return productInfoListinCart;
	}
	public int getCartTotalPrice() {
		return cartTotalPrice;
	}
	public String getEmptyCartMessage() {
		return emptyCartMessage;
	}
	public void setEmptyCartMessage(String emptyCartMessage) {
		this.emptyCartMessage = emptyCartMessage;
	}



}
