package CheckBot.CheckBot;

import java.io.IOException;
import java.util.List;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

/**
 * Hello world!
 *
 */
public class App 
{
	private static String botHash="1942950848:AAG5ZEWSVvyGKkCw_yXFAWFr12068oVmv1Q";
	private static String numberOfChat="-752006228";
	
	public static void main(String[] args) throws InterruptedException {

		System.out.print("Устанавливаю подключение с Ботом ... ");
	    TelegramBot bot = new TelegramBot(botHash);
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
	    Long lll=new Long(numberOfChat);
	    foo(bot,lll);
	    System.out.print("Финиш");
	    bot.removeGetUpdatesListener();
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
		Integer a=listMessage(chatId,bot);
		Integer b=a;
		sendMessage2Bot(chatId,bot);
		while(a.intValue()==b.intValue()) {
			Thread.sleep(10*60-1);
			a=listMessage(chatId,bot);
		}
	}
	private static void sendMessage2Bot(Long chatId, String messaggio,TelegramBot bot)  {
		SendMessage sf=new SendMessage(chatId,messaggio);
	    SendResponse sendResponse = bot.execute(sf);
	    boolean ok = sendResponse.isOk();
	    Message message = sendResponse.message();
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
	private static  Integer listMessage(Long chatId,TelegramBot bot) {
//		System.err.println(new GetUpdates().getLimit());
		GetUpdates getUpdates = new GetUpdates().limit(1).offset(-1).timeout(0);
		GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
		List<Update> updates = updatesResponse.updates();
//		int i=0;
		Message message;
		Integer mId=0;
		for(Update up: updates) {
			//System.err.print(i + " ");		
			message = up.message();
//		     Chat chat = message.chat();
		     User user = message.from();
	        if (message.text() != null) { 
	            System.out.println("up=" + up.updateId() + "New message: " + message.text() + " id: " + message.messageId() + " from " + user.username());
	            mId=message.messageId();
	        }
//	        i++;
		}	
		return mId;
	}
}
