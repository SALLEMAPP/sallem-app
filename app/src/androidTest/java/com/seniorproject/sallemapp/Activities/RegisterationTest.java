package com.seniorproject.sallemapp.Activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.seniorproject.sallemapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RegisterationTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void registerationTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_register), withText("Register"),
                        withParent(allOf(withId(R.id.activity_welcome),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.registeration_txtfirstName),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.registeration_txtfirstName),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.registeration_txtfirstName),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("Fisal"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.registeration_txtLastName),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Assubaieye"), closeSoftKeyboard());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.registeration_txtemail),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("fisal0707@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.registeration_txtPassword),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("Vanilla"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.registeration_txtConfirmPassword),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("Vanilla"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.registration_btnAvatar),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.registeration_cbAgree), withText("I Agree to terms of use"),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.Btn_resgisteration), withText("Register"),
                        withParent(allOf(withId(R.id.activity_registration),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.sign_in_txt_user_name),
                        withParent(allOf(withId(R.id.activity_sign_in),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatEditText8.perform(click());

    }

}
