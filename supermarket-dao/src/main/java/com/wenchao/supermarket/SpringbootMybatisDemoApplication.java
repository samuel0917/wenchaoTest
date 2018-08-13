package com.wenchao.supermarket;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import com.wenchao.supermarket.db.bean.Smsverify;
import com.wenchao.supermarket.db.mapper.SmsverifyMapper;

@SpringBootApplication
@MapperScan("com.wenchao.supermarket.db.mapper")
public class SpringbootMybatisDemoApplication {

	private static Logger log=LoggerFactory.getLogger(SpringbootMybatisDemoApplication.class);
	
	
	public static void main(String[] args) {
		ApplicationContext applicationContext =SpringApplication.run(SpringbootMybatisDemoApplication.class, args);
		SmsverifyMapper smsDao= applicationContext.getBean(SmsverifyMapper.class);
		test(smsDao);
    }
	
	public static void test(final SmsverifyMapper smsDao){
		CloudAccount account = new CloudAccount(
                ServiceSettings.getMNSAccessKeyId(),
                ServiceSettings.getMNSAccessKeySecret(),
                ServiceSettings.getMNSAccountEndpoint());
        MNSClient client = account.getMNSClient(); //this client need only initialize once

        // Demo for receive message code
        try{
             CloudQueue queue = client.getQueueRef("cloud-queue-demo");// replace with your queue name
             int count=0;
             while(true){
        		 Message popMsg = queue.popMessage();
	                if (popMsg != null){
	                	log.info("start message .\n"+new Date());
	                    log.info("message handle: " + popMsg.getReceiptHandle());
	                    log.info("message body: " + popMsg.getMessageBodyAsString());
	                    log.info("message id: " + popMsg.getMessageId());
	                    log.info("message dequeue count:" + popMsg.getDequeueCount());
	                    //<<to add your special logic.>>
//	                    Thread.sleep(8000);
	                   
	                    Smsverify info=smsDao.selectByPrimaryKey(Long.valueOf(1));
	                    if(info!=null&&info.getStatusId().equals("1")){
	                    	info.setContent("test"+count);
	                    	info.setStatusId("3");
	                    	smsDao.updateByPrimaryKey(info);
	                    	log.info("update success..."+info.getContent());
	                    	count++;
	                    	try {
								Thread.sleep(8000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
	                    }
	                    
	                    //remember to  delete message when consume message successfully.
	                    queue.deleteMessage(popMsg.getReceiptHandle());
	                    log.info("delete message successfully.\n"+new Date());
	                }
          }
        } catch (ClientException ce)
        {
            log.info("Something wrong with the network connection between client and MNS service."
                    + "Please check your network and DNS availablity.");
            ce.printStackTrace();
        } catch (ServiceException se)
        {
            if (se.getErrorCode().equals("QueueNotExist"))
            {
                log.info("Queue is not exist.Please create queue before use");
            } else if (se.getErrorCode().equals("TimeExpired"))
            {
                log.info("The request is time expired. Please check your local machine timeclock");
            }
            /*
            you can get more MNS service error code in following link.
            https://help.aliyun.com/document_detail/mns/api_reference/error_code/error_code.html?spm=5176.docmns/api_reference/error_code/error_response
            */
            se.printStackTrace();
        } catch (Exception e)
        {
            log.info("Unknown exception happened!");
            e.printStackTrace();
        }

        client.close();
	}
}
