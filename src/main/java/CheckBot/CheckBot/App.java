package CheckBot.CheckBot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.pengrad.telegrambot.model.Chat;
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
 * This application is for restricting rights and granting rights to the user in a telegram chat.
 * For a volunteer project "
 * Creater Dmitrii Lebedev 
 * December 2022
 */
public class App 
{
	private static String fileResource="resource.json";
    private static Long chatMainId=(long) 0;
    private static Long newUserId=(long) 0;
    private static Map infoChatUser;
    private static FileWriter record;
    private static Map firstAnswer;
    
    public static void SystemLog(String text)     {
    	/* функция вывода событий в журнал
    	 * переменная record должна быть уже опредена
    	 */
    	Calendar Today=Calendar.getInstance();  // определим сегодняшнюю дату и установим дату из параметров            
        try {
        	int month=Today.get(Calendar.MONTH)+1;
			record.write(Today.get(Calendar.YEAR) + "." + month + "." + Today.get(Calendar.DATE) + " " + Today.get(Calendar.HOUR_OF_DAY) + ":" + Today.get(Calendar.MINUTE) + ":" + Today.get(Calendar.SECOND) + " " + text + "\n");
	        record.flush();
		} catch (IOException e) {
			System.out.println("Ошибка записи в журнал " + e.getMessage());
			e.printStackTrace();
		}

    }
	public static void main(String[] args) throws InterruptedException {
		/*
		 * Подключение к боту и запуск слушателя
		 */
		System.out.print("Устанавливаю подключение с Ботом ... ");
		infoChatUser=new HashMap<>();
		firstAnswer=new HashMap<>();
		String botHash=getStringFromJson("BotHash");
	    TelegramBot bot = new TelegramBot(botHash);
	    try {
			record=new FileWriter("CheckBot.log",true);
		} catch (IOException e1) {
			// 
			System.err.println("Ошибка создания лог файла CheckBot.log" + e1.getMessage());
			e1.printStackTrace();
		}
	    bot.setUpdatesListener(updates->{
			    for (Update update : updates) {
			    	if(update.message()!=null) {
			    		if(update.message().leftChatMember()!=null)  // Пользователь покинул чат
			    			SystemLog("Пользователь " + update.message().leftChatMember().firstName() + " " + update.message().leftChatMember().lastName() + " @" + update.message().leftChatMember().username() + " покинул чат " + update.message().chat().title());
			    		if(update.message().entities()!=null) {  // пользователь "запустил" разговор с ботом в отдельном чате
			    			try {
								startBot(update.message().chat().id(),bot); // разговор с ботом
								GetChatResponse getChatResponse = bot.execute(new GetChat(chatMainId));
								GetMeResponse getMeResponse=bot.execute(new GetMe());							
								GetChatMemberResponse getChatMember=bot.execute(new GetChatMember(update, chatMainId));
								getChatResponse.chat().permissions().canSendMessages(true);
								BaseResponse response = bot.execute(new RestrictChatMember(chatMainId,newUserId).canSendMessages(true));
								SystemLog("Пользователю " + ((User)infoChatUser.get(newUserId)).lastName() + " @" + ((User)infoChatUser.get(newUserId)).username() + " выданы права для записи");
								getChatResponse = bot.execute(new GetChat(chatMainId));
								int countAnswer=firstAnswer.size();
								for(int i=0;i<countAnswer;i++) {	// удаляем сообщения в основном чате, которые мы написали при входе нового пользователя
									DeleteMessage deleteMessage0 = new DeleteMessage(chatMainId, (int) firstAnswer.get(newUserId + "_" + i));
									BaseResponse response0 = bot.execute(deleteMessage0);
									if(response0.isOk()) {
										SystemLog("Удалено сообщение " + (int) firstAnswer.get(newUserId + "_" + i) + " от Бота после пользователя " + ((User)infoChatUser.get(newUserId)).lastName() + " @" + ((User)infoChatUser.get(newUserId)).username());
										firstAnswer.remove(newUserId + "_" + i);
									}
									else
										SystemLog("!!! Ошибка удаления сообщений от Бота после пользователя " + ((User)infoChatUser.get(newUserId)).lastName() + " @" + ((User)infoChatUser.get(newUserId)).username() + "(" + response0.description() + ")");
								}
								
								
							} catch (InterruptedException e) {
								// Ошибка
								SystemLog("Ошибка удаления сообщений от Бота, ниже стек вызова " + e.getMessage());
								e.printStackTrace();
							}
			    		}
			    		if(update.message().newChatMembers()!=null) { // к чату подключился новый пользователь
			    			for(int i=0;i<update.message().newChatMembers().length; i++) {
			    				SystemLog("В чат вошел новый пользователь " + update.message().newChatMembers()[i].lastName() + " @" + update.message().newChatMembers()[i].username());			    				
			    				chatMainId=update.message().chat().id();
			    				newUserId=update.message().newChatMembers()[i].id();
			    				infoChatUser.put(chatMainId, update.message().chat());
			    				infoChatUser.put(newUserId,update.message().newChatMembers()[i]);
								BaseResponse response = bot.execute(new RestrictChatMember(chatMainId,newUserId).canSendMessages(false));
								if(response.isOk())
									SystemLog("Для чата charId=" +chatMainId + " " + update.message().chat().title() + " и нового пользователя " + update.message().newChatMembers()[i].lastName() + " @" + update.message().newChatMembers()[i].username() + " установлен режим чтения");
								else
									SystemLog("Ошибка!!! Для чата charId=" +chatMainId + " " + update.message().chat().title() + " и нового пользователя " + update.message().newChatMembers()[i].lastName() + " @" + update.message().newChatMembers()[i].username() + " НЕ установлен режим чтения!!!");

								Map firstMessage=getMapFromJson("firstMessage","newuser");
								for(int j=0;j<firstMessage.size(); j++) {
									String temp;
									switch (j) { // добавим некоторые данные информационных сообщений в основном чате 
									case 0:
										temp=update.message().newChatMembers()[i].firstName() + " " + update.message().newChatMembers()[i].lastName();
										break;
									case 1:
										temp=getStringFromJson("usernameBot");
										break;
									default:
										temp="";	
									}
									
									firstAnswer.put(newUserId + "_" +j,sendMessage2Bot(update.message().chat().id(),(String) firstMessage.get("newuser" + j) + temp,bot));
								}

			    			}
			    			
			    		}

			    	}
    	
			    }
			    return UpdatesListener.CONFIRMED_UPDATES_ALL;  // все данные слушателя прослушаны и обработаны
			}
		);

	    System.out.println(" Ok!!!");

	    System.out.println("Слушатель запущен " );


	}

	public static void startBot(Long chatId,TelegramBot bot) throws InterruptedException {
		/*
		 * запуск "разговора" бота с новым участником чата
		 */

		Map textBegin=getMapFromJson("textBegin","begin");
		Map timeout=getMapFromJson("textBegin","timeout");
		Map ans=getMapFromJson("answer","ans");
		String question=getStringFromJson("question");
		int timeoutAnswer=getIntFromJson("timeoutAnswer");
		Map reaction=getMapFromJson("reaction","ans");		
		Map go=getMapFromJson("reaction","get");
		Map check=getMapFromJson("check","a");
		Map correct=getMapFromJson("check","b");

		
		boolean flag=true;
		while(true) {  // цикл вопросов бота
			if(flag) {
				sendMessage2Bot(chatId, textBegin, "begin", timeout, "timeout", bot);
			}
					
			Map answer=sendAnswer2Bot(chatId,ans,bot, question,timeoutAnswer);
			if(((String) answer.get("timeout")).equalsIgnoreCase("false")) {
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
					Map ansCheck=sendAnswer2Bot(chatId,check,bot, strNext,timeoutAnswer);
					if(((String) ansCheck.get("timeout")).equalsIgnoreCase("false")) {
						String label1=findMap(check,(String) ansCheck.get("text"));					
						String anstrue=label1.replace('a', 'b');
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
			
		}
		
	}
	
	private static String findMap(Map ans, String answer) {
		/*
		 * Find answer from List
		 * Обратный поиск, по значению найти ключ
		 */
		String keyReturn="";
		for(Object key:  ans.keySet()) {
			String a=(String) ans.get(key);
			if(a.equalsIgnoreCase(answer))
				keyReturn=(String) key;
		}
		return	keyReturn;
	}
	private static Map getMapFromJson(String ans,String name) {
		/*
		 * Загрузить данные из json файла в Map
		 */
		// Read Map from Json file
        //JSON parser object to parse read file
		Map mapAns=new HashMap<>();
        InputStream is=null;
		try {
			is = new FileInputStream(fileResource);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			SystemLog("Ошибка открытия файла ресурсов " + fileResource +  " " + e.getMessage() );
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
			SystemLog("Ошибка закрытия файла ресурсов " + fileResource +  " " + e.getMessage() );
			e.printStackTrace();
        }
		return mapAns;
	}
	private static Map sendAnswer2Bot(Long chatId, Map ans, TelegramBot bot, String question, int timeoutAnswer) throws InterruptedException {
		/*
		 * Определение ответа пользователя на вопрос от бота 
		 */
		
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
		// read Integer from Json
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

	private static Integer sendMessage2Bot(Long chatId, String messaggio,TelegramBot bot)  {
		SendMessage sf=new SendMessage(chatId,messaggio);
	    SendResponse sendResponse = bot.execute(sf);
	    boolean ok = sendResponse.isOk();
	    Message message = sendResponse.message();
	    Integer id=message.messageId();
	    return id;
	}
	private static void sendMessage2Bot(Long chatId, String messaggio,TelegramBot bot,Keyboard replyKeyboardMarkup)  {
		SendMessage sf=new SendMessage(chatId,messaggio);
		sf.replyMarkup(replyKeyboardMarkup);
	    SendResponse sendResponse = bot.execute(sf);
	    boolean ok = sendResponse.isOk();
	    Message message = sendResponse.message();
	}

	private static  Map listMessage(Long chatId,TelegramBot bot) {
		/*
		 * поиск сообщения в чате
		 */
		Map mes=new HashMap<>();
		GetUpdates getUpdates = new GetUpdates().limit(1).offset(-1).timeout(0);
		GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
		List<Update> updates = updatesResponse.updates();
		Message message;
		Integer mId=0;
		mes.put("id", mId);
		for(Update up: updates) {
			message = up.message();
			if(message!=null) {
			     User user = message.from();
		        if (message.text() != null) { 
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


	/*
	 * наброски для модификации
	 * 	
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
	private static void sendMessage2Bot(Long chatId, String question) {
		// For text
		System.out.println(question);
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
		*/
}
