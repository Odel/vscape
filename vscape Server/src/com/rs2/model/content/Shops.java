package com.rs2.model.content;

import com.rs2.model.content.quests.DwarfCannon;
import com.rs2.model.content.quests.QuestHandler;
import com.rs2.model.players.Player;
import com.rs2.model.players.ShopManager;

/**
 * Created by IntelliJ IDEA. User: vayken Date: 5/16/12 Time: 1:16 AM To change
 * this template use File | Settings | File Templates.
 */
public class Shops {

	public static int findShop(Player player, int npcId) {
		switch (npcId) {
			case 3161:
				return 0;
	
			case 672:
				return 1;
	
			case 590:
				return 2;
	
			case 2161:
				return 3;
	
			case 1688:
				return 4;
	
			case 525:
			case 524:
				return 5;
	
			case 3030:
				return 6;
	
			case 1862:
				return 8;
	
			case 2307:
				return 9;
	
			case 638:
				return 10;
	
			case 571:
				return 11;
	
			case 573:
				return 12;
	
			case 570:
				return 13;
				
			case 569:
				return 14;
				
			case 572:
				return 15;
	
			case 563:
				return 16;
	
			case 3824:
				return 18;
                            
			case 3541:
				return 20;
	
			case 692:
				return 21;
	
			case 1917:
				return 22;
	
			case 731:
				return 23;
	
			case 597:
				return 24;
	
			case 547:
				return 25;
	
			case 1039:
				return 26;
	
			case 2258:
				return 27;
	
			case 583:
				return 28;
	
			case 519:
				return 30;
	
			case 471:
				return 31;
	
			case 2343:
				return 32;
	
			case 1860:
				return 33;
	
			case 559:
				return 34;
	
			case 1083:
				return 35;
	
			case 1834:
				return 36;
	
			case 2158:
				return 37;
	
			case 577:
				return 38;
	
			case 1434:
				return 39;
	
			case 873:
				return 40;
	
			case 2324:
				return 41;
	
			case 683:
				return 42;
	
			case 1781:
				return 43;
	
			case 588:
				return 44;
	
			case 545:
				return 45;
	
			case 2233:
				return 46;
	
			case 2335:
				return 47;
	
			case 579:
				return 48;
	
			case 582:
				return 49;
	
			case 520:
			case 521:
				return 50;
	
			case 528:
			case 529:
				return 104;
			
			case 530:
				return 151;

			case 531:
				return 126;
	
			case 532:
			case 533:
				return 94;
	
			case 1785:
				return 51;
	
			case 1782:
				return 52;
	
			case 2331:
				return 53;
	
			case 2323:
				return 54;
	
			case 1369:
				return 55;
	
			case 522:
			case 523:
				return 151;
	
			case 554:
				return 57;
	
			case 2342:
				return 58;
	
			case 517:
				return 59;
	
			case 592:
				return 60;
	
			case 580:
				return 61;
	
			case 2327:
				return 62;
	
			case 1393:
				return 63;
	
			case 593:
				return 65;
	
			case 578:
				return 66;
			case 1316:
				return 64;
	
			case 603:
				return 68;
	
			case 586:
				return 69;
			case 2330:
				return 70;
	
			case 540:
				return 71;
	
			case 1040:
				return 72;
	
			case 2720:
			case 558:
				return 73;
	
			case 2344:
				return 74;
	
			case 305:
				return 75;
	
			case 2157:
				return 76;
	
			case 874:
				return 77;
	
			case 556:
				return 78;
	
			case 602:
				return 79;
	
			case 2154:
				return 80;
	
			case 1437:
				return 81;
	
			case 576:
				return 82;
	
			case 538:
				return 83;
	
			case 584:
				return 84;
	
			case 2340:
				return 85;
	
			case 575:
				return 86;
	
			case 549:
				return 87;
	
			case 1779:
				return 88;
	
			case 1436:
				return 89;
	
			case 566:
				return 90;
	
			case 560:
				return 92;
	
			case 564:
				return 93;
	
			case 568:
				return 95;
	
			case 2156:
				return 96;
	
			case 527:
			case 526:
				return 56;
	
			case 933:
				return 98;
	
			case 2325:
				return 99;
	
			case 932:
				return 100;
	
			case 542:
				return 101;
	
			case 550:
				return 103;
	
			case 903:
				return 105;
	
			case 2326:
				return 106;
	
			case 1658:
				return 107;
	
			case 461:
				return 108;
	
			case 209:
				return 110;
	
			case 594:
				return 111;
	
			case 2160:
				return 112;
	
			case 3206:
				return 113;
	
			case 1866:
				return 115;
	
			case 1699:
				return 116;
	
			case 2153:
				return 117;
	
			case 2152:
				return 118;
	
			case 544:
				return 120;
	
			case 1972:
				return 121;
	
			case 1254:
				return 122;
	
			case 2334:
				return 123;
	
			case 2306:
				return 124;
	
			case 1783:
				return 125;
	
			case 601:
				return 127;
	
			case 585:
				return 128;
	
			case 1038:
				return 129;
	
			case 1787:
				return 130;
	
			case 2304:
				return 131;
	
			case 537:
				return 132;
	
			case 3038:
				return 133;
	
			case 2332:
				return 134;
	
			case 1282:
				return 135;
	
			case 1786:
				return 136;
	
			case 1433:
				return 137;
	
			case 2024:
				return 138;
	
			case 2336:
				return 139;
	
			case 793:
				return 140;
	
			case 1980:
				return 141;
	
			case 548:
				return 142;
			case 596:
				return 143;
			case 2338:
				return 144;
			case 2341:
				return 146;
			case 2622:
				return 147;
			case 2620:
				return 148;
			case 2623:
				return 149;
			case 2305:
				return 150;
			case 551:
			case 552:
				return 152;
			case 2333:
				return 153;
			case 581:
				return 154;
			case 1778:
				return 155;
			case 557:
				return 156;
			case 1301:
				return 157;
			case 546:
				return 158;
			case 534:
			case 535:
				return 159;
			case 541:
				return 160;
			case 589:
				return 161;
			case 1596:
			case 1597:
			case 1598:
			case 1599:
			case 70:
				return 162; //slayer equipment
			case 587:
				return 163;
			case 1303:
				return 164;
			case 747 :
			    if(player.getQuestStage(15) >= 9){
				return 165;
			    }
			break;
			/*case 797: //
				return 166;*/
			case 797: //hero shop
				return 168;
			case 1208://halberd
				return 169;
			case 1923://ancient staff
			case 1925://ancient staff
				return 170;
			case 1680://crystal
				return 171;
			case 1921:
			case 849:
			case 736:
			case 734:
			case 1700: //ghost innkeeper
			case 733: //bartenders
				return 194;
			case 4295:  // Warrior's Guild Equipment shop
				return 196;
			case 4293:  //Warrior's Guild Food Shop
				return 197;
			case 4359: //patchy
				return 198;
			case 1334: //jossik
				return 199;
			case 536: //valaine
				return 200;
			case 4294:  //Warrior's Guild Potion Shop
				return 195;
			case 555:
				return 97;	//khazard general store	- cadillac
			case 516:
				return 193; //Shilo village general store      	
			case 553:
                            // if(player.getSkill().getLevel()[Skill.RUNECRAFTING] == 99) //auburry my dick so far
				return 19;
			case 836 :
				return 201;
			case 1435:
				return 203;
			case 970:
				return 204;
			case 971:
				return 205;
                       /* case 961:
                            if(player.getSkill().getLevel()[Skill.HITPOINTS] == 99) //Surgeon
                                return 179;
                            else return -1;
                        case 805:
                             if(player.getSkill().getLevel()[Skill.CRAFTING] == 99) //Master crafter
                                 return 183;
                             else return -1;
                                    */
	
			/*
				return 165;
			case 536:
				return 168;
			case 516:
				return 169;
			case 488:
				return 170;
			case 1780:
				return 171;
			case 1784:
				return 172;*/ //All non functional, missing data.
		}
		return -1;
	}

	public static boolean openShop(Player player, int npcId) {
		if(player.onApeAtoll() && !player.getMMVars().isMonkey()) {
		    player.getActionSender().sendMessage("You must be disguised as a monkey to trade this shopkeep.");
		    return false;
		}
		int shop = findShop(player, npcId);
		if (shop > -1) {
		    if(npcId != DwarfCannon.NULODION) {
			ShopManager.openShop(player, shop);
			return true;
		    } else {
			if(QuestHandler.questCompleted(player, 30)) {
			    ShopManager.openShop(player, shop);
			    return true;
			} else {
			    player.getActionSender().sendMessage("You must complete Dwarf Cannon to shop here.");
			    return true;
			}
		    }
		}
		return false;
	}
}
