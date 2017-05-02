package com.atinternet;

import com.atinternet.tracker.Context;
import com.atinternet.tracker.CustomVar;
import com.atinternet.tracker.Gesture;
import com.atinternet.tracker.MediaPlayer;
import com.atinternet.tracker.Order;
import com.atinternet.tracker.Publisher;
import com.atinternet.tracker.Screen;
import com.atinternet.tracker.SelfPromotion;
import com.atinternet.tracker.SetConfigCallback;
import com.atinternet.tracker.Tracker;
import com.atinternet.tracker.TrackerConfigurationKeys;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Integration {


    public ArrayList<Operation> getOperations() {
        return operations;
    }

    private ArrayList<Operation> operations = new ArrayList<>();

    public Integration(final Tracker tracker) {
        operations.add(new Operation(1).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 1);
                Screen s = tracker.Screens().add("Home", "USA", "Texas", "Dallas").setLevel2(8);
                s.Location(34.5877676, 35.711123);
                s.CustomObjects().add(new HashMap<String, Object>() {{
                    put("Country", "Azerbaïdjan");
                    put("id", "36A13TU");
                }});
                s.Campaign("CampaignId");
                s.CustomTreeStructure(4).setCategory2(3).setCategory3(3);
                s.InternalSearch("veryGood  Se(°)arch", 8).setResultPosition(4);
                s.SelfPromotions().add(65);
                s.CustomVars().add(5, "FR", CustomVar.CustomVarType.App);
                s.CustomVars().add(5, "Europe", CustomVar.CustomVarType.Screen);

                s.sendView();
            }
        }));
        operations.add(new Operation(2).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 2);
                Gesture gesture = tracker.Gestures().add("Navigation", "Move");
                gesture.CustomObjects().add("{\"id\":\"36A13TU\"}");
                gesture.sendNavigation();
            }
        }));
        operations.add(new Operation(3).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 3);
                Gesture gesture = tracker.Gestures().add("Touch", "Move");
                gesture.sendTouch();
            }
        }));
        operations.add(new Operation(4).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 4);
                Gesture gesture = tracker.Gestures().add("Download", "Move");
                gesture.sendDownload();
            }
        }));
        operations.add(new Operation(5).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 5);
                Gesture gesture = tracker.Gestures().add("Exit", "Move");
                gesture.sendExit();
            }
        }));
        operations.add(new Operation(6).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 6);
                Gesture gesture = tracker.Gestures().add("Search", "Move");
                gesture.InternalSearch("littleBox", 7);
                gesture.sendSearch();
            }
        }));
        operations.add(new Operation(7).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 7);
                final Publisher pub = tracker.Publishers().add("c1")
                        .setAdvertiserId("adv")
                        .setCreation("cre")
                        .setDetailedPlacement("dp")
                        .setFormat("f")
                        .setVariant("v")
                        .setGeneralPlacement("gp")
                        .setUrl("url.org");
                pub.CustomObjects().add(new HashMap<String, Object>() {{
                    put("localized", "true");
                }});
                pub.sendImpression();
            }
        }));
        operations.add(new Operation(8).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 8);
                final Publisher pub = tracker.Publishers().add("c1")
                        .setAdvertiserId("adv")
                        .setCreation("cre")
                        .setDetailedPlacement("dp")
                        .setFormat("f")
                        .setVariant("v")
                        .setGeneralPlacement("gp")
                        .setUrl("url.org");
                pub.CustomObjects().add(new HashMap<String, Object>() {{
                    put("localized", "true");
                }});
                pub.sendTouch();
            }
        }));
        operations.add(new Operation(9).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 9);
                SelfPromotion selfPromotion = tracker.SelfPromotions().add(3)
                        .setFormat("f")
                        .setProductId("pdt");
                selfPromotion.CustomObjects().add(new HashMap<String, Object>() {{
                    put("localized", "true");
                }});
                selfPromotion.sendImpression();
            }
        }));
        operations.add(new Operation(10).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 10);
                SelfPromotion selfPromotion = tracker.SelfPromotions().add(3)
                        .setFormat("f")
                        .setProductId("pdt");
                selfPromotion.CustomObjects().add(new HashMap<String, Object>() {{
                    put("localized", "true");
                }});
                selfPromotion.sendTouch();
            }
        }));
        operations.add(new Operation(11).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 11);
                tracker.DynamicScreens().add("screenId", "newName", new Date()).setChapter1("chapter").sendView();
            }
        }));
        operations.add(new Operation(12).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 12);
                tracker.Context().setBackgroundMode(Context.BackgroundMode.Task);
                tracker.Context().setLevel2(5);
                tracker.IdentifiedVisitor().set(54, 57);
                tracker.Screens().add("Home", "USA", "Texas", "Dallas").sendView();
            }
        }));
        operations.add(new Operation(13).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 13);
                tracker.Context().setBackgroundMode(Context.BackgroundMode.Normal);
                tracker.IdentifiedVisitor().unset();
                tracker.Context().setLevel2(0);
                tracker.Products().add("1");
                tracker.Products().add("2");
                tracker.Products().add("3");
                tracker.Products().add("4");
                tracker.Products().sendViews();
            }
        }));
        final MediaPlayer player = tracker.Players().add(4);
        operations.add(new Operation(14).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 14);
                player.Videos().add("Movie", 90)
                        .setLevel2(3)
                        .sendPlay(0);
            }
        }));
        operations.add(new Operation(15).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 15);
                player.LiveVideos().add("Stream")
                        .sendPlay(0);
            }
        }));
        operations.add(new Operation(16).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 16);
                player.Audios().add("GoodSong", 3)
                        .sendPlay(0);
            }
        }));
        operations.add(new Operation(17).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 17);
                player.LiveAudios().add("Radio")
                        .sendPlay(0);
            }
        }));
        operations.add(new Operation(18).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 18);
                tracker.Cart().set("cartId")
                        .Products().add("Shoes")
                        .setPromotionalCode("NEW")
                        .setQuantity(2)
                        .setDiscountTaxFree(2)
                        .setDiscountTaxIncluded(3)
                        .setUnitPriceTaxFree(4)
                        .setUnitPriceTaxIncluded(6);
                Screen s = tracker.Screens().add("Shopping").setIsBasketScreen(true);
                Order o = s.Order("orderId", 90);
                o.CustomVars().add(6, "customShop");
                o.setStatus(2)
                        .setPaymentMethod(3)
                        .Amount().set(3, 4, 1)
                        .Delivery().set(3, 4, "1")
                        .Discount().set(3, 4, "NEW")
                        .setNewCustomer(true);
                s.sendView();
                tracker.Cart().unset();
            }
        }));
        operations.add(new Operation(19).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 19);
                Screen s = tracker.Screens().add("Aisle");
                s.Aisle("lvl1").setLevel2("lvl2");
                s.sendView();
            }
        }));
        operations.add(new Operation(20).setSendAction(new Runnable() {
            @Override
            public void run() {
                tracker.setParam("testId", 20);
                tracker.setConfig(new HashMap<String, Object>() {{
                    put(TrackerConfigurationKeys.SECURE, true);
                    put(TrackerConfigurationKeys.HASH_USER_ID, true);
                    put(TrackerConfigurationKeys.SITE, 123456);
                }}, false, new SetConfigCallback() {
                    @Override
                    public void setConfigEnd() {
                        tracker.setParam("idclient", "Custom-ClientID");
                        tracker.Screens().add().sendView();
                    }
                });
            }
        }));
    }
}
