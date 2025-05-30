/*    
    Copyright (C) Paul Falstad and Usevalad Khatkevich
    
    This file is part of CircuitJS1.

    CircuitJS1 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 2 of the License, or
    (at your option) any later version.

    CircuitJS1 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with CircuitJS1.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.lushprojects.circuitjs1.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Button;
import com.lushprojects.circuitjs1.client.util.Locale;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class ModDialog extends DialogBox {
	
	VerticalPanel vp;
	Storage lstor = Storage.getLocalStorageIfSupported();
	
	//for "UI scale:"
	HorizontalPanel scaleButtons;
	Button setScale100Button;
	Button setDefaultScaleButton;
	Button setScaleButton;
	HTML scaleScrollbarElm;
	native float getRealScale() /*-{
		return $wnd.nw.Window.get().zoomLevel;
	}-*/;
	String getScaleScrollbar(float value,int scale){
		return "<input type=\"range\" id=\"scaleUI\" oninput=\"getScaleInfo()\"" +
			"min=\"-0.5\" max=\"2\" step=\"0.1\" value=\""+value+"\"" +
			"style=\"width:355px\"><b><span class=\"scaleInfo\"" +
			"style=\"vertical-align:super\">"+scale+"%</span></b>";
	}

	//for "Top menu bar:"
	HorizontalPanel topMenuBarVars;
	CheckBox setStandartTopMenu;
	CheckBox setSmallTopMenu;

	//for "Start/Stop and Reset buttons:"
	HorizontalPanel btnsPreview;
	HTML previewText;
	Button resetPrevBtn;
	Button stopPrevBtn;
	HorizontalPanel SRCheckBoxes;
	VerticalPanel vp1;
	CheckBox setDefaultSRBtns;
	CheckBox setClassicSRBtns;
	VerticalPanel vp2;
	CheckBox setStopIcon;
	CheckBox setPauseIcon;
	VerticalPanel vp3;
	CheckBox hideSRBtns;
	native boolean CirSimIsRunning()/*-{
		return $wnd.CircuitJS1.isRunning();
	}-*/;
	CheckBox setShowSidebaronStartup;
	CheckBox setOverlayingSidebar;

	HorizontalPanel SBAnimationSettings;
	TextBox DurationSB;
	CheckBox setAnimSidebar;
	ListBox SpeedCurveSB;

	int getSpeedCurveSBIndex(String val){
		for (int i=0;i<=SpeedCurveSB.getItemCount();i++){
			if (SpeedCurveSB.getValue(i)==val)
			return i;
		}
		return 1;
	}

	CheckBox setPauseWhenWinUnfocused;

	Button closeButton;

	ModDialog() {

		super();
		vp = new VerticalPanel();
		setWidget(vp);
		setText("Modification Setup");
		vp.setWidth("400px");

		vp.add(new HTML("<big><b>UI Scale:</b></big>"));
		vp.add(scaleScrollbarElm = new HTML(getScaleScrollbar(getRealScale(),(int)(getRealScale()*100+100))));
		vp.setCellHorizontalAlignment(scaleScrollbarElm, HasHorizontalAlignment.ALIGN_CENTER);
		vp.add(scaleButtons = new HorizontalPanel());
		scaleButtons.setWidth("100%");
		scaleButtons.add(setScale100Button = new Button("100%",
			new ClickHandler(){
				public void onClick(ClickEvent event) {
					vp.remove(scaleScrollbarElm);
					vp.insert(scaleScrollbarElm = new HTML(getScaleScrollbar(0,100)),1);
					vp.setCellHorizontalAlignment(scaleScrollbarElm, HasHorizontalAlignment.ALIGN_CENTER);
					CirSim.executeJS("setScaleUI()");
					lstor.setItem("MOD_UIScale", "0");
				}
			}));
		scaleButtons.add(setDefaultScaleButton = new Button("Default scale<b>*</b>",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					vp.remove(scaleScrollbarElm);
					vp.insert(scaleScrollbarElm = new HTML(getScaleScrollbar(CirSim.getDefaultScale(),
						(int)(CirSim.getDefaultScale()*100+100))),1);
					vp.setCellHorizontalAlignment(scaleScrollbarElm, HasHorizontalAlignment.ALIGN_CENTER);
					CirSim.executeJS("setScaleUI()");
					lstor.setItem("MOD_UIScale", Float.toString(CirSim.getDefaultScale()));
				}
			}));
		scaleButtons.add(setScaleButton = new Button("Set",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					CirSim.executeJS("setScaleUI()");
					lstor.setItem("MOD_UIScale", Float.toString(getRealScale()));
				}
			}));
		vp.add(new HTML("<p>* - the default UI scale for your monitor is set to "+
			(int)(CirSim.getDefaultScale()*100+100)+"%</p>"));
		// Styling buttons:
		setScaleButton.addStyleName("modButtons"); //.setHeight("20px");
		setScaleButton.addStyleName("modSetButtons");
		setScale100Button.addStyleName("modButtons");
		setDefaultScaleButton.addStyleName("modButtons");
		//remove "gwt-Button" style:
		setScaleButton.removeStyleName("gwt-Button");
		setScale100Button.removeStyleName("gwt-Button");
		setDefaultScaleButton.removeStyleName("gwt-Button");
		scaleButtons.setCellHorizontalAlignment(setScale100Button, HasHorizontalAlignment.ALIGN_CENTER);
		scaleButtons.setCellHorizontalAlignment(setDefaultScaleButton, HasHorizontalAlignment.ALIGN_CENTER);
		scaleButtons.setCellHorizontalAlignment(setScaleButton, HasHorizontalAlignment.ALIGN_CENTER);

		vp.add(new HTML("<hr><big><b>Top Menu Bar:</b></big>"));
		vp.add(topMenuBarVars = new HorizontalPanel());
		topMenuBarVars.setWidth("100%");
		topMenuBarVars.add(setStandartTopMenu = new CheckBox("Standart"));
		topMenuBarVars.add(setSmallTopMenu = new CheckBox("Small"));
		if (CirSim.MENUBARHEIGHT<30) setSmallTopMenu.setValue(true);
		else setStandartTopMenu.setValue(true);

		setStandartTopMenu.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (setSmallTopMenu.getValue()) {
					CirSim.MENUBARHEIGHT = 30;
					//CirSim.layoutPanel.setWidgetSize(menuBar, 30);
					setSmallTopMenu.setValue(false);
					setStandartTopMenu.setValue(true);
					CirSim.executeJS("CircuitJS1.redrawCanvasSize()");
					lstor.setItem("MOD_TopMenuBar", "standart");
					CirSim.executeJS("setAllAbsBtnsTopPos(\""+CirSim.getAbsBtnsTopPos()+"px\")");
				} else {
					setStandartTopMenu.setValue(true);
				}
			}
		});

		setSmallTopMenu.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (setStandartTopMenu.getValue()) {
					CirSim.MENUBARHEIGHT = 20;
					//CirSim.layoutPanel.setWidgetSize(menuBar, 19);
					setStandartTopMenu.setValue(false);
					setSmallTopMenu.setValue(true);
					CirSim.executeJS("CircuitJS1.redrawCanvasSize()");
					lstor.setItem("MOD_TopMenuBar", "small");
					CirSim.executeJS("setAllAbsBtnsTopPos(\""+CirSim.getAbsBtnsTopPos()+"px\")");
				} else {
					setSmallTopMenu.setValue(true);
				}
			}
		});

		// Styling checkboxes:
		topMenuBarVars.setCellHorizontalAlignment(setStandartTopMenu, HasHorizontalAlignment.ALIGN_CENTER);
		topMenuBarVars.setCellHorizontalAlignment(setSmallTopMenu, HasHorizontalAlignment.ALIGN_CENTER);
		
		vp.add(new HTML("<hr><big><b>Start/Stop and Reset Buttons:</b></big>"));
		vp.add(btnsPreview = new HorizontalPanel());
		btnsPreview.setWidth("100%");
		btnsPreview.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		btnsPreview.add(previewText = new HTML("<big>Preview:</big>"));
		btnsPreview.add(resetPrevBtn = new Button("&#8634;"));
		btnsPreview.add(stopPrevBtn = new Button("&#xE800;"));
		if (lstor.getItem("MOD_absBtnIcon")=="pause")
			stopPrevBtn.getElement().setInnerHTML("&#xE802;");
		btnsPreview.setCellHorizontalAlignment(previewText, HasHorizontalAlignment.ALIGN_RIGHT);
		btnsPreview.setCellHorizontalAlignment(resetPrevBtn, HasHorizontalAlignment.ALIGN_RIGHT);
		btnsPreview.setCellHorizontalAlignment(stopPrevBtn, HasHorizontalAlignment.ALIGN_LEFT);
		
		stopPrevBtn.setStyleName("run-stop-btn modPrevBtn");
		resetPrevBtn.setStyleName("reset-btn modPrevBtn");
		if (lstor.getItem("MOD_absBtnTheme")=="default"){
			stopPrevBtn.addStyleName("modDefaultRunStopBtn");
			resetPrevBtn.addStyleName("modDefaultResetBtn");
		} else {
			stopPrevBtn.addStyleName("gwt-Button");
			resetPrevBtn.addStyleName("gwt-Button");
		}

		vp.add(SRCheckBoxes = new HorizontalPanel());
		SRCheckBoxes.setWidth("100%");
		SRCheckBoxes.add(vp1 = new VerticalPanel());
		SRCheckBoxes.add(vp2 = new VerticalPanel());
		SRCheckBoxes.add(vp3 = new VerticalPanel());
		vp3.setHeight("100%");
		SRCheckBoxes.setCellVerticalAlignment(vp3, HasVerticalAlignment.ALIGN_MIDDLE);
		//SRCheckBoxes.setCellHorizontalAlignment(vp3, HasHorizontalAlignment.ALIGN_CENTER);

		vp1.add(new HTML("<b>Theme:</b>"));
		vp1.add(setDefaultSRBtns = new CheckBox("Default"));
		vp1.add(setClassicSRBtns = new CheckBox("Classic"));

		vp2.add(new HTML("<b>Icon:</b>"));
		vp2.add(setStopIcon = new CheckBox("Stop"));
		vp2.add(setPauseIcon = new CheckBox("Pause"));

		vp3.add(hideSRBtns = new CheckBox("HIDE BUTTONS"));

		if (CirSim.absResetBtn.getElement().hasClassName("modDefaultResetBtn"))
			setDefaultSRBtns.setValue(true);
		else setClassicSRBtns.setValue(true);

		/*if (CirSim.absRunStopBtn.getElement().getInnerText() == "&#xE800;")
			setStopIcon.setValue(true);
		else setPauseIcon.setValue(true);*/ //try to get info from localstorage

		if (lstor.getItem("MOD_absBtnIcon")=="stop") setStopIcon.setValue(true);
		else setPauseIcon.setValue(true);

		if (lstor.getItem("MOD_hideAbsBtns")=="true") hideSRBtns.setValue(true);

		setDefaultSRBtns.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setClassicSRBtns.getValue()) {
						setClassicSRBtns.setValue(false);
						setDefaultSRBtns.setValue(true);
						//Buttons for preview:
						stopPrevBtn.removeStyleName("gwt-Button");
						stopPrevBtn.addStyleName("modDefaultRunStopBtn");
						resetPrevBtn.removeStyleName("gwt-Button");
						resetPrevBtn.addStyleName("modDefaultResetBtn");
						//Absolute buttons:
						CirSim.absRunStopBtn.removeStyleName("gwt-Button");
						CirSim.absRunStopBtn.removeStyleName("modClassicButton");
						CirSim.absRunStopBtn.addStyleName("modDefaultRunStopBtn");
						CirSim.absResetBtn.removeStyleName("gwt-Button");
						CirSim.absResetBtn.removeStyleName("modClassicButton");
						CirSim.absResetBtn.addStyleName("modDefaultResetBtn");
						//save:
						lstor.setItem("MOD_absBtnTheme", "default");
					} else {
						setDefaultSRBtns.setValue(true);
					}
				}
			});
		setClassicSRBtns.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setDefaultSRBtns.getValue()) {
						setDefaultSRBtns.setValue(false);
						setClassicSRBtns.setValue(true);
						//Buttons for preview:
						stopPrevBtn.removeStyleName("modDefaultRunStopBtn");
						stopPrevBtn.addStyleName("gwt-Button");
						resetPrevBtn.removeStyleName("modDefaultResetBtn");
						resetPrevBtn.addStyleName("gwt-Button");
						//Absolute buttons:
						CirSim.absRunStopBtn.removeStyleName("modDefaultRunStopBtn");
						CirSim.absRunStopBtn.addStyleName("gwt-Button");
						CirSim.absRunStopBtn.addStyleName("modClassicButton");
						CirSim.absResetBtn.removeStyleName("modDefaultResetBtn");
						CirSim.absResetBtn.addStyleName("gwt-Button");
						CirSim.absResetBtn.addStyleName("modClassicButton");
						//save:
						lstor.setItem("MOD_absBtnTheme", "classic");
					} else {
						setClassicSRBtns.setValue(true);
					}
				}
			});
		setStopIcon.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setPauseIcon.getValue()) {
						setPauseIcon.setValue(false);
						setStopIcon.setValue(true);
						stopPrevBtn.getElement().setInnerHTML("&#xE800;");
						if (CirSimIsRunning())
							CirSim.absRunStopBtn.getElement().setInnerHTML("&#xE800;");
						//save:
						lstor.setItem("MOD_absBtnIcon", "stop");
					} else {
						setStopIcon.setValue(true);
					}
				}
			});
		setPauseIcon.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setStopIcon.getValue()) {
						setStopIcon.setValue(false);
						setPauseIcon.setValue(true);
						stopPrevBtn.getElement().setInnerHTML("&#xE802;");
						if (CirSimIsRunning())
							CirSim.absRunStopBtn.getElement().setInnerHTML("&#xE802;");
						//save:
						lstor.setItem("MOD_absBtnIcon", "pause");
					} else {
						setPauseIcon.setValue(true);
					}
				}
			});
		hideSRBtns.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (hideSRBtns.getValue()){
						CirSim.absRunStopBtn.setVisible(false);
						CirSim.absResetBtn.setVisible(false);
						//save:
						lstor.setItem("MOD_hideAbsBtns", "true");
					}else{
						CirSim.absRunStopBtn.setVisible(true);
						CirSim.absResetBtn.setVisible(true);
						//save:
						lstor.setItem("MOD_hideAbsBtns", "false");
					}
				}
			});

		vp.add(new HTML("<hr><big><b>Sidebar:</b></big>"));
		vp.add(setOverlayingSidebar = new CheckBox("Sidebar is overlaying"));
		vp.setCellVerticalAlignment(setOverlayingSidebar, HasVerticalAlignment.ALIGN_TOP);
		//vp.setCellHorizontalAlignment(setOverlayingSidebar, HasHorizontalAlignment.ALIGN_CENTER);
		
		vp.add(SBAnimationSettings = new HorizontalPanel());
		SBAnimationSettings.setWidth("100%");
		SBAnimationSettings.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		SBAnimationSettings.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		SBAnimationSettings.add(setAnimSidebar = new CheckBox("Animation:"));
		SBAnimationSettings.setCellHorizontalAlignment(setAnimSidebar, HasHorizontalAlignment.ALIGN_LEFT);
		SBAnimationSettings.add(new Label("duration is"));
		SBAnimationSettings.add(DurationSB = new TextBox());
		DurationSB.setMaxLength(3);DurationSB.setVisibleLength(1);
		DurationSB.setHeight("0.6em");
		DurationSB.setAlignment(TextAlignment.CENTER);
		SBAnimationSettings.add(new Label("ms,"));
		SBAnimationSettings.add(new Label("speed curve is"));
		SBAnimationSettings.add(SpeedCurveSB = new ListBox());
		SpeedCurveSB.getElement().setAttribute("style","appearance:none;padding:1px;");
		SpeedCurveSB.addItem("ease");
		SpeedCurveSB.addItem("linear");
		SpeedCurveSB.addItem("ease-in");
		SpeedCurveSB.addItem("ease-out");
		SpeedCurveSB.addItem("ease-in-out");

		vp.add(setShowSidebaronStartup = new CheckBox("Show sidebar on startup"));
		//vp.setCellHorizontalAlignment(setShowSidebaronStartup, HasHorizontalAlignment.ALIGN_CENTER);

		if (lstor.getItem("MOD_overlayingSidebar")=="true") setOverlayingSidebar.setValue(true);
		else setAnimSidebar.setEnabled(false);
		if (lstor.getItem("MOD_overlayingSBAnimation")=="true") setAnimSidebar.setValue(true);

		DurationSB.setValue(lstor.getItem("MOD_SBAnim_duration"));
		SpeedCurveSB.setItemSelected(getSpeedCurveSBIndex(lstor.getItem("MOD_SBAnim_SpeedCurve")),true);

		DurationSB.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (!Character.isDigit(event.getCharCode()) || DurationSB.getValue()=="0") {
				((TextBox) event.getSource()).cancelKey();
				}
			}
		});

		DurationSB.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					lstor.setItem("MOD_SBAnim_duration", DurationSB.getValue());
					if (setOverlayingSidebar.getValue()) CirSim.setSidebarAnimation(DurationSB.getValue(),SpeedCurveSB.getSelectedItemText());
				}
			});

		SpeedCurveSB.addChangeHandler(new ChangeHandler() {
				public void onChange(ChangeEvent event) {
					lstor.setItem("MOD_SBAnim_SpeedCurve", SpeedCurveSB.getSelectedItemText());
					if (setOverlayingSidebar.getValue())
						CirSim.setSidebarAnimation(DurationSB.getValue(),SpeedCurveSB.getSelectedItemText());
				}
			});

		setOverlayingSidebar.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setOverlayingSidebar.getValue()){
						lstor.setItem("MOD_overlayingSidebar", "true");
						setAnimSidebar.setEnabled(true);
						if (setAnimSidebar.getValue()) CirSim.setSidebarAnimation(DurationSB.getValue(),SpeedCurveSB.getSelectedItemText());
					} else {
						lstor.setItem("MOD_overlayingSidebar", "false");
						setAnimSidebar.setEnabled(false);
						CirSim.setSidebarAnimation("none","");
					}
				}
			});
		setAnimSidebar.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setAnimSidebar.getValue()){
						lstor.setItem("MOD_overlayingSBAnimation", "true");
						if (setOverlayingSidebar.getValue())
							CirSim.setSidebarAnimation(DurationSB.getValue(),SpeedCurveSB.getSelectedItemText());
					} else {
						lstor.setItem("MOD_overlayingSBAnimation", "false");
						CirSim.setSidebarAnimation("none","");
					}
				}
			});

		if (lstor.getItem("MOD_showSidebaronStartup")=="true") setShowSidebaronStartup.setValue(true);
		setShowSidebaronStartup.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setShowSidebaronStartup.getValue()){
						lstor.setItem("MOD_showSidebaronStartup", "true");
					} else {lstor.setItem("MOD_showSidebaronStartup", "false");}
				}
			});

		vp.add(new HTML("<hr><big><b>Other:</b></big>"));
		vp.add(setPauseWhenWinUnfocused = new CheckBox("Pause simulation when window loses focus<br>(recommended for optimal performance)",true));
		vp.setCellHorizontalAlignment(setPauseWhenWinUnfocused, HasHorizontalAlignment.ALIGN_CENTER);
		if (lstor.getItem("MOD_setPauseWhenWinUnfocused")=="true") setPauseWhenWinUnfocused.setValue(true);
		setPauseWhenWinUnfocused.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if (setPauseWhenWinUnfocused.getValue()){
						lstor.setItem("MOD_setPauseWhenWinUnfocused", "true");
					} else {lstor.setItem("MOD_setPauseWhenWinUnfocused", "false");}
				}
			});
		vp.add(new HTML("<br>"));

		vp.add(closeButton = new Button("<b>Close</b>",
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					closeDialog();
				}
			}));
		vp.setCellHorizontalAlignment(closeButton,
			HasHorizontalAlignment.ALIGN_CENTER);

		center();
		show();
	}

	protected void closeDialog() {
		hide();
	}

}
