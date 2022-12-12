package CheckBot.CheckBot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONTokener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.ChosenInlineResult;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.MenuButton;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InlineQueryResultGif;
import com.pengrad.telegrambot.model.request.InlineQueryResultMpeg4Gif;
import com.pengrad.telegrambot.model.request.InlineQueryResultPhoto;
import com.pengrad.telegrambot.model.request.InlineQueryResultVideo;
import com.pengrad.telegrambot.model.request.InputLocationMessageContent;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetChat;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.RestrictChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatMenuButton;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetChatMemberResponse;
import com.pengrad.telegrambot.response.GetChatResponse;
import com.pengrad.telegrambot.response.GetMeResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * Hello world!
 *
 */
public class App 
{
	private static String botHash="1942950848:AAG5ZEWSVvyGKkCw_yXFAWFr12068oVmv1Q";
	//private static String numberOfChat="-1001756121759";
	private static String fileResource="/home/dima/eclipse-workspace/CheckBot/resource.json";
    private static Long chatMainId=(long) 0;
    private static Long newUserId=(long) 0;
    private static Integer id0;
    private static Integer id1;
    private static Integer id2;
	public static void main(String[] args) throws InterruptedException {

		System.out.print("Устанавливаю подключение с Ботом ... ");
	    TelegramBot bot = new TelegramBot(botHash);

	    bot.setUpdatesListener(updates->{
			    for (Update update : updates) {
			    	System.out.println("update= " + update.updateId());
			    	if(update.chatJoinRequest()!=null)
			    		System.out.println(update.chatJoinRequest().from().lastName());
			    	if(update.channelPost()!=null)
			    		System.out.println("channelPost()" + update.channelPost().text());
			    	if(update.chatMember()!=null)
			    		System.out.println("chatMember()" + update.chatMember().newChatMember().user().lastName());
			    	if(update.chosenInlineResult()!=null)
			    		System.out.println("chosenInlineResult()" + update.chosenInlineResult().from().lastName());
			    	if(update.inlineQuery()!=null)
			    		System.out.println("inlineQuery()" + update.inlineQuery().query());
			    	if(update.message()!=null) {
			    		System.out.println("message()" + update.message().chat().title());
			    		if(update.message().leftChatMember()!=null)
			    			System.out.println("chat left " + update.message().leftChatMember().firstName() + " " + update.message().leftChatMember().lastName());
			    		if(update.message().chat()!=null) {
			    			System.out.println("bio() " + update.message().chat().description() );
			    		}
			    		if(update.message().contact()!=null)
			    			System.out.println("contact " + update.message().contact().lastName());
			    		if(update.message().entities()!=null) {
			    			try {
								startBot(update.message().chat().id(),bot);
								GetChatResponse getChatResponse = bot.execute(new GetChat(chatMainId));
								GetMeResponse getMeResponse=bot.execute(new GetMe());							
								GetChatMemberResponse getChatMember=bot.execute(new GetChatMember(update, chatMainId));						
								System.out.println("canSendMessages()" + getChatResponse.chat().permissions().canSendMessages());
								System.out.println("firstname()" + getMeResponse.user().firstName());								
								//System.out.println("canSendMessages2()" + getChatMember.chatMember().canSendMessages());
								getChatResponse.chat().permissions().canSendMessages(true);
								System.out.print("charId=" +chatMainId + " newUserId=" + newUserId);
								BaseResponse response = bot.execute(new RestrictChatMember(chatMainId,newUserId).canSendMessages(true));
								System.out.println(" ?=" + response.isOk());
								getChatResponse = bot.execute(new GetChat(chatMainId));
								DeleteMessage deleteMessage0 = new DeleteMessage(chatMainId, id0);
								BaseResponse response0 = bot.execute(deleteMessage0);
								DeleteMessage deleteMessage1 = new DeleteMessage(chatMainId, id1);
								BaseResponse response1 = bot.execute(deleteMessage1);
								DeleteMessage deleteMessage2 = new DeleteMessage(chatMainId, id2);
								BaseResponse response2 = bot.execute(deleteMessage2);
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    		}
			    		if(update.message().forumTopicCreated()!=null)
			    			System.out.println("forumTopicCreate " + update.message().forumTopicCreated().name());
			    		if(update.message().forwardFrom()!=null)
			    			System.out.println("forwardFrom " + update.message().forwardFrom().lastName());
			    		if(update.message().newChatMembers()!=null) {
			    			System.out.print("new user ");
			    			for(int i=0;i<update.message().newChatMembers().length; i++) {
			    				chatMainId=update.message().chat().id();
			    				newUserId=update.message().newChatMembers()[i].id();
								System.out.print("Для чата charId=" +chatMainId + " и нового пользователя newUserId=" + newUserId + " установлен режим чтения");
								BaseResponse response = bot.execute(new RestrictChatMember(chatMainId,newUserId).canSendMessages(false));
								System.out.println(" ?=" + response.isOk());
			    				System.out.print(update.message().newChatMembers()[i].lastName() + " " + newUserId + " " );
			    				String newuser0=getStringFromJson("newuser0");
			    				String newuser1=getStringFromJson("newuser1");
			    				String newuser2=getStringFromJson("newuser2");
			    				String usernameBot=getStringFromJson("usernameBot");
			    				id0=sendMessage2Bot(update.message().chat().id(),newuser0 + update.message().newChatMembers()[i].firstName() + " " + update.message().newChatMembers()[i].lastName(),bot);		    				
			    				id1=sendMessage2Bot(update.message().chat().id(),newuser1 + usernameBot, bot);
			    				id2=sendMessage2Bot(update.message().chat().id(),newuser2,bot);
			    			}
			    			System.out.println();
			    		}
			    		if(update.message().location()!=null)
			    			System.out.println("location " + update.message().location().toString());
			    	}
			    	if(update.myChatMember()!=null)
			    		System.out.println("myChatMember()" + update.myChatMember().from().lastName());
			    	if(update.preCheckoutQuery()!=null)
			    		System.out.println("preCheckoutQuery()" + update.preCheckoutQuery().toString());
			    	if(update.shippingQuery()!=null)
			    		System.out.println("shippingQuery()" + update.shippingQuery().from().lastName());
			    	if(update.poll()!=null)
			    		System.out.println("poll()" + update.poll().question());
			    	
			    	
			    	
			    	
			    }
			    return UpdatesListener.CONFIRMED_UPDATES_ALL;
			}
		);
/*	    bot.setUpdatesListener(new UpdatesListener() {
			@Override
			public int process(List<Update> updates) {
				   for (Update update : updates) {
					   	 Message message = update.message();
					     Chat chat = message.chat();
					     User user = message.from();
					        if (message.text() != null) {
					            System.out.println("New message: " + message.text() + " id: " + message.messageId() + " from " + chat);
					            //SendResponse sendResponse = bot.execute(new SendMessage(chat.id(), "Selber: " + message.text()));
					        }
					      }
			    return UpdatesListener.CONFIRMED_UPDATES_ALL;
			}
		});
*/
	    System.out.println(" Ok!!!");
	    //Long lll=new Long(numberOfChat);
	    	
	    //foo(bot,lll);
	    //startBot(lll,bot);
	    System.out.print("Финиш " );

	    ///bot.removeGetUpdatesListener();
	}

	public static void startBot(Long chatId,TelegramBot bot) throws InterruptedException {

		Map textBegin=getMapFromJson("textBegin","begin");
		Map timeout=getMapFromJson("textBegin","timeout");
		//List<Integer> timeout = new ArrayList<Integer>() ;
		//timeout.add(getIntFromJson("timeout1"));
		//timeout.add(getIntFromJson("timeout2"));
		//String badfinish=getStringFromJson("badfinish");
		//String next=getStringFromJson("next");
		Map ans=getMapFromJson("answer","ans");
		String question=getStringFromJson("question");
		int timeoutAnswer=getIntFromJson("timeoutAnswer");
		Map reaction=getMapFromJson("reaction","ans");		
		Map go=getMapFromJson("reaction","get");
		Map check=getMapFromJson("check","a");
		Map correct=getMapFromJson("check","b");
		//reaction=getMapFromJson("reaction");
		
		boolean flag=true;
		while(true) {
			if(flag) {
				sendMessage2Bot(chatId, textBegin, "begin", timeout, "timeout", bot);
			}
					
			Map answer=sendAnswer2Bot(chatId,ans,bot, question,timeoutAnswer);
			if(((String) answer.get("timeout")).equalsIgnoreCase("false")) {
				//sendMessage2Bot(chatId, "Хмм.. твой ответ: " + answer.get("text"),bot);
				String label=findMap(ans,(String) answer.get("text"));
				String gokey=label.replace('a', 'g').replace('n', 'e').replace('s', 't');
			    String mes=(String) reaction.get(label);
			    sendMessage2Bot(chatId, mes,bot);
			    String next=(String) go.get(gokey);
			    if(next.equalsIgnoreCase("question")) {
			    	flag=false;
			    	continue;
			    }
			    if(next.equalsIgnoreCase("textBegin")) {
			    	flag=true;
			    	continue;
			    }
			    if(next.equalsIgnoreCase("badfinish")) 
			    	continue;
			    if(next.equalsIgnoreCase("next")) {
			    	String strNext=getStringFromJson(next);
			    	//sendMessage2Bot(chatId, strNext,bot);
					Map ansCheck=sendAnswer2Bot(chatId,check,bot, strNext,timeoutAnswer);
					if(((String) ansCheck.get("timeout")).equalsIgnoreCase("false")) {
						String label1=findMap(check,(String) ansCheck.get("text"));					
						String anstrue=label1.replace('a', 'b');
						System.out.println("anstrue=" + correct.get(anstrue) + " ansCheck=" + ansCheck.get("text") + " label1=" + label1);
						String a=(String) correct.get(anstrue);
						if(a.equalsIgnoreCase("true")) {
							String goodfinish=getStringFromJson("goodfinish");
							sendMessage2Bot(chatId, goodfinish,bot);
							break;
						}
						String badfinish=getStringFromJson("badfinish");
						sendMessage2Bot(chatId, badfinish,bot);					
					}
			    }						    		
			}
			else {
				flag=false;
				continue;
			}
			
			//break;
			/*
			String label=findMap(ans,answer);
			if(label.equalsIgnoreCase("textBegin")) 
				continue;
			if(label.equalsIgnoreCase("question")) {
				flag=false;
				continue;
			}
			if(label.equalsIgnoreCase("badfinish")) {
				sendMessage2Bot(chatId, badfinish);
				break;
			}
			if(label.equalsIgnoreCase("next")) {
				sendMessage2Bot(chatId, next);
				int count=getIntFromJson("countqustion");
				int rnd=(int) (Math.random()+count);
				String q=getStringFromJson("question" + rnd);
				sendMessage2Bot(chatId, q);
				
				
			}
			*/
		}
		
	}
	private static void insertOuterChat(TelegramBot bot, Long chatId) {
		// Goto Other Chat
		  InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(new InlineKeyboardButton[]{
			      new InlineKeyboardButton("ddd").url("https://t.me/+TLfVpbnrWGNlZThi")});
		InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
		        new InlineKeyboardButton[]{
		                new InlineKeyboardButton("Вход в чат").url("https://t.me/+TLfVpbnrWGNlZThi"),
		                new InlineKeyboardButton("callback_data").callbackData("callback_data"),
		                new InlineKeyboardButton("Game").callbackGame("94%")
		                //new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query")
		        });
		BaseResponse response = bot.execute(new SendMessage(chatId,"key").replyMarkup(keyboard));
		//SendMessage sf=new SendMessage(chatId,"key");
		//sf.replyMarkup(inlineKeyboard);
		//SendResponse sendResponse = bot.execute(sf);
		//BaseResponse response; //=bot.execute(sendResponse.message().messageId())		
		//sendMessage2Bot(chatId,"Кнопка",bot,inlineKeyboard);
	}

	private static void sendMessage2Bot(Long chatId, String question) {
		// For text
		System.out.println(question);
	}
	private static String findMap(Map ans, String answer) {
		// Find answer from List
		String keyReturn="";
		for(Object key:  ans.keySet()) {
			String a=(String) ans.get(key);
			if(a.equalsIgnoreCase(answer))
				keyReturn=(String) key;
		}
		return	keyReturn;
	}
	private static Map getMapFromJson(String ans,String name) {
		// Read Map from Json file
        //JSON parser object to parse read file
		Map mapAns=new HashMap<>();
        InputStream is=null;
		try {
			is = new FileInputStream(fileResource);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + fileResource);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray courses = object.getJSONArray(ans);
        for (int i = 0; i < courses.length(); i++) {
        	JSONObject objecti=(JSONObject) courses.get(i);        	
        	mapAns.put(name+i, objecti.get(name+i));
        }
        try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		return mapAns;
	}
	private static Map sendAnswer2Bot(Long chatId, Map ans, TelegramBot bot, String question, int timeoutAnswer) throws InterruptedException {
		// TODO Auto-generated method stub
		String[] name1 = new String [ans.size()/2];
		String[] name2 = new String [ans.size()-ans.size()/2];
		int i=0;
		for(Object key:  ans.keySet()) {
			if(i<ans.size()/2)
				name1[i]=(String) ans.get(key);
			else {
				int j=i-ans.size()/2;
				name2[j]=(String) ans.get(key);
			}
			i++;
		}
		
		SendResponse sendResponse = bot.execute(new SendMessage(chatId, question)
			      .replyMarkup(new ReplyKeyboardMarkup(
			                name1,name2)
			                .oneTimeKeyboard(true)   // optional
			                //.resizeKeyboard(true)    // optional
			                //.selective(true)));        // optional
							));
		BaseResponse response; 
		Integer a=(Integer) listMessage(chatId,bot).get("id");
		Integer b=a;
		int ii=0;
		Map mes=new HashMap<>();
		//sendMessage2Bot(chatId,bot);
		while(a.intValue()==b.intValue() && ii<timeoutAnswer) {
			Thread.sleep(10*60-1);
			mes=listMessage(chatId,bot);
			mes.put("timeout", "false");
			a=(Integer) mes.get("id");
			ii++;
		}
		if(ii>=timeoutAnswer)
			mes.put("timeout", "true");
		return mes;
	}
	private static String getStringFromJson(String string) {
		// read String from Json
        InputStream is=null;
		try {
			is = new FileInputStream(fileResource);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + fileResource);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		return (String) object.getString(string);
	}
	private static int getIntFromJson(String string) {
		// read String from Json
        InputStream is=null;
		try {
			is = new FileInputStream(fileResource);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + fileResource);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		return object.getInt(string);
	}
	private static void sendMessage2Bot(Long chatId, Map text, String key1, Map timeout, String key2, TelegramBot bot) throws InterruptedException  {
		// Print message plus timeout
		for(int i=0;i<text.size();i++) {
			sendMessage2Bot(chatId,(String) text.get(key1 + i),bot);
			int m=(int)timeout.get(key2 + i);
			Thread.sleep(m*60-1);
		}
			
		
	}
	private static String[] getTextArrayFromJson(String string) {
		// Read text array from Json
		
		return null;
	}
	public static void foo(TelegramBot bot,Long chatId) throws InterruptedException {
		String messaggio="Привет я Бог...";
		sendMessage2Bot(chatId, messaggio,bot);
		Thread.sleep(10*60-1);
		sendMessage2Bot(chatId, "...",bot);
		Thread.sleep(100*60-1);
		sendMessage2Bot(chatId, "Извини, ошибся, я бот",bot);
		boolean isSelective=true;
		Keyboard forceReply = new ForceReply(isSelective);
		Keyboard replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"first row button1", "first row button2"},
                new String[]{"second row button1", "second row button2"})
                .oneTimeKeyboard(true)   // optional
                .resizeKeyboard(true)    // optional
                .selective(true);        // optional	
		
		Keyboard keyboard = new ReplyKeyboardMarkup(
		        new KeyboardButton[]{
		                new KeyboardButton("text"),
		                new KeyboardButton("contact").requestContact(true),
		                new KeyboardButton("location").requestLocation(true)
		        }        
		);  	
		InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
		        new InlineKeyboardButton[]{
		                new InlineKeyboardButton("url").url("www.google.com"),
		                new InlineKeyboardButton("callback_data").callbackData("callback_data"),
		                new InlineKeyboardButton("Game").callbackGame("94%")
		                //new InlineKeyboardButton("Switch!").switchInlineQuery("switch_inline_query")
		        });
		//sendMessage2Bot(chatId,"Кнопка",bot,keyboard);
		//UpdatesListener.CONFIRMED_UPDATES_ALL;
		/*
		Integer a=listMessage(chatId,bot);
		Integer b=a;
		sendMessage2Bot(chatId,bot);
		while(a.intValue()==b.intValue()) {
			Thread.sleep(10*60-1);
			a=listMessage(chatId,bot);
		}
		*/
	}
	private static Integer sendMessage2Bot(Long chatId, String messaggio,TelegramBot bot)  {
		SendMessage sf=new SendMessage(chatId,messaggio);
	    SendResponse sendResponse = bot.execute(sf);
	    boolean ok = sendResponse.isOk();
	    Message message = sendResponse.message();
	    Integer id=message.messageId();
	    return id;
	    //sendDocument(sendDocumentRequest);
	}
	private static void sendMessage2Bot(Long chatId, String messaggio,TelegramBot bot,Keyboard replyKeyboardMarkup)  {
		SendMessage sf=new SendMessage(chatId,messaggio);
		sf.replyMarkup(replyKeyboardMarkup);
	    SendResponse sendResponse = bot.execute(sf);
	    boolean ok = sendResponse.isOk();
	    Message message = sendResponse.message();
	    
	    //sendDocument(sendDocumentRequest);
	}
	private static void sendMessage2Bot(Long chatId,TelegramBot bot) {
		SendResponse sendResponse = bot.execute(new SendMessage(chatId, "Я должен у тебя узнать. Ты кто?")
			      .replyMarkup(new ReplyKeyboardMarkup(
			                new String[]{"Я бот, мы братья", "Я человек"},
			                new String[]{"Это секрет", "А ты кто?"})
			                .oneTimeKeyboard(true)   // optional
			                //.resizeKeyboard(true)    // optional
			                //.selective(true)));        // optional
							));
		BaseResponse response; //=bot.execute(sendResponse.message().messageId())
		//System.out.println(sendResponse.message().caption() + "\n" + sendResponse.message().chat());
	}
	private static  Map listMessage(Long chatId,TelegramBot bot) {
//		System.err.println(new GetUpdates().getLimit());
		Map mes=new HashMap<>();
		GetUpdates getUpdates = new GetUpdates().limit(1).offset(-1).timeout(0);
		GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
		List<Update> updates = updatesResponse.updates();
//		int i=0;
		Message message;
		Integer mId=0;
		mes.put("id", mId);
		for(Update up: updates) {
			//System.err.print(i + " ");
			//aaa(bot,up);
			message = up.message();
//		     Chat chat = message.chat();
			if(message!=null) {
			     User user = message.from();
		        if (message.text() != null) { 
		            System.out.println("up=" + up.updateId() + " New message: " + message.text() + " id: " + message.messageId() + " from " + user.username() 
		              );
		            mes.put("id",message.messageId());
		            mes.put("text",message.text());
		            mes.put("user", user.username());
		            mes.put("username", user.firstName() + " " + user.lastName());
		            mId=message.messageId();
		            
		        }
			}
		}	
		return mes;
	}
	public static void aaa(TelegramBot bot,Update update ) {
		
		InlineQueryResult r1 = new InlineQueryResultPhoto("id", "photoUrl", "thumbUrl");
		InlineQueryResult r2 = new InlineQueryResultArticle("id", "title", "message text").thumbUrl("url");
		InlineQueryResult r3 = new InlineQueryResultGif("id", "gifUrl", "thumbUrl");
		InlineQueryResult r4 = new InlineQueryResultMpeg4Gif("id", "mpeg4Url", "thumbUrl");

		InlineQueryResult r5 = new InlineQueryResultVideo(
		  "id", "videoUrl", InlineQueryResultVideo.MIME_VIDEO_MP4, "message", "thumbUrl", "video title")
		    .inputMessageContent(new InputLocationMessageContent(21.03f, 105.83f));
		
			InlineQuery inlineQuery = update.inlineQuery();
			ChosenInlineResult chosenInlineResult = update.chosenInlineResult();
			CallbackQuery callbackQuery = update.callbackQuery();
	
			BaseResponse response = bot.execute(new AnswerInlineQuery(inlineQuery.id(), r1, r2, r3, r4, r5));
	
			int cacheTime=10;
			boolean isPersonal=true;
			// or full
			bot.execute(
			        new AnswerInlineQuery(inlineQuery.id(), new InlineQueryResult[]{r1, r2, r3, r4, r5})
			                .cacheTime(cacheTime)
			                .isPersonal(isPersonal)
			                .nextOffset("offset")
			                .switchPmParameter("pmParam")
			                .switchPmText("pmText")
			);
	}
}
