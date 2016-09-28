package net.psoft.mainapp;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Login Test
 *
 * @author chenzp
 * @date 2015/11/4
 * @Copyright:重庆平软科技有限公司
 */
@SuppressWarnings("rawtypes")
public class LoginActivityTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.angma.framework.android.mainapp.activity.LoginActivity";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public LoginActivityTest() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    /**登录测试*/
    public void testLogin() {
        solo.enterText((android.widget.EditText) solo.getView("et_userName"), "admin");
        solo.enterText((android.widget.EditText) solo.getView("et_pass"), "123456");
        solo.takeScreenshot();
        solo.clickOnButton("登录");
        //solo.assertCurrentActivity("测试失败", "TabHostActivity");
        try{
            solo.wait(4000);
        }catch (Exception e){
            e.printStackTrace();
        }
        solo.clickOnText("系统");
        solo.clickOnText("修改登录密码");
        solo.clickOnText("确定");
    }
}
