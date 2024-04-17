**API Endpoints**
------------------------------------------------------------------------------------------------------------------------

GET /video/status/{id}

ResponseBody {
    "result" : "COMPLETED"/"PENDING"/"ERROR"
}

Returns the status of the video with the given id.

POST /video/upload/
RequestBody {
    "email":
    "videoLink":
}

ResponseBody {
    "id"
    "status"
    "email"
    "uploadTime"
}

Posts a video to the server for processing. Responds with the video metadata.

Implementation
------------------------------------------------------------------------------------------------------------------------
**VideoController**

-Defines the API endpoints and calls the appropriate service to serve the request


**VideoService**

-Serves as an abstraction layer between VideoController and VideoManager.

-Implements methods that are needed from Controllers

**VideoManager**

-Manages the data structures for videos


**VideoProcessorThread**

-Thread that handles the logic for updating the statuses of processing videos

-It also notifies the user via email when video processing is complete or an error has occurred.

-It also handles retry logic allowing the video to be reprocessed up to 5 times


**DLQService**

-Updates the status of the video back to PENDING as it sends it back the queue for reprocessing.

-Implementation is rather bare here. Things that devs can plan on implementing is persisting entries that are on the DLQ.
This would ensure that if DLQ goes down for whatever reason there will be a back-up on our tables.


**NotificationService**

-Intializes JavaMailSender

-sends emails

Testing
--------------------------------------------------------------------------------------------------------------------------
To run tests go to test/java/com.example.HeyGen/

Run each of the test files.

**DLQTest**

-This tests the functionality of the DLQ and retries when an error occurrs while processing.

-The DLQ tries to reprocess the video by adding it back to the queue.

If after 5 retries it fails then it will not be added onto the dlq and instead an email will be sent to the user notifying them that there was an error
And the video status will stay as ERROR


**VideoProcessorTest**

-This tests the functionality of the processing queue.

-After processing the video an email will be sent to the user confirming the completion of the video processing.


Postman
------------------------------------------------------------------------------------------------------------------------
-Feel free to test using Postman
-Run main. Start making requests in Postman
-I chose not to make it configurable in the request body because the user shouldn't need to provide that sort of information
-Devs can configure the DEFAULT_UPLOAD_TIME in VideoManager
