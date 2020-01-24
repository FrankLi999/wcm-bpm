import * as StompJs from '@stomp/stompjs';
// import * as SockJS from 'sockjs-client';
import { AppComponent } from './app.component';

export class WebSocketAPIV5 {
    webSocketEndPoint: string = 'http://localhost:8080/ws';
    // webSocketEndPoint: string = 'ws://localhost:8080/ws';
    topic: string = "/topic/greetings";
    stompClient: any;
    appComponent: AppComponent;
    constructor(appComponent: AppComponent){
        this.appComponent = appComponent;
    }
    _connect() {
        console.log("Initialize WebSocket Connection");
        // let ws = new SockJS(this.webSocketEndPoint);
        // this.stompClient = StompJs.over(ws);
        
        this.stompClient = new StompJs.Client({
            brokerURL: this.webSocketEndPoint
            // connectHeaders: {
            //   login: "user",
            //   passcode: "password"
            // },
            // debug: function (str) {
            //   console.log(str);
            // },
            // reconnectDelay: 5000,
            // heartbeatIncoming: 4000,
            // heartbeatOutgoing: 4000
        });

        this.stompClient.onConnect = function(frame) {
            // Do something, all subscribes must be done is this callback
            // This is needed because this will be executed after a (re)connect
            this.stompClient.subscribe(this.topic, function (message) {
                this.onMessageReceived(message);
            });
            //_this.stompClient.reconnect_delay = 2000;
        };

        // this.stompClient.connect({}, function (frame) {
        //     this.stompClient.subscribe(this.topic, function (sdkEvent) {
        //         this.onMessageReceived(sdkEvent);
        //     });
        //     //_this.stompClient.reconnect_delay = 2000;
        // }, this.errorCallBack);

        this.stompClient.onStompError = this.errorCallBack;

        this.stompClient.activate();
    };

    _disconnect() {
        if (this.stompClient !== null) {
            // this.stompClient.disconnect();
            this.stompClient.deactivate();
        }
        
        console.log("Disconnected");
    }

    // on error, schedule a reconnection attempt
    errorCallBack(frame) {
        // Will be invoked in case of error encountered at Broker
        // Bad login/passcode typically will cause an error
        // Complaint brokers will set `message` header with a brief message. 
        // Body may contain details.
        // Compliant brokers will terminate the connection after any error
        console.log('Broker reported error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
        console.log("errorCallBack -> " + frame);
        setTimeout(() => {
            this._connect();
        }, 5000);
    }

	/**
	 * Send message to sever via web socket
	 * @param {*} message 
	 */
    _send(message) {
        console.log("calling logout api via web socket");
        this.stompClient.send("/app/hello", {}, JSON.stringify(message));
        // this.stompClient.publish({destination: '/topic/general', body: 'Hello world'});

        // There is an option to skip content length header
        // this.stompClient.publish({destination: '/topic/general', body: 'Hello world', skipContentLengthHeader: true});
    
        // Additional headers
        // this.stompClient.publish({destination: '/topic/general', body: 'Hello world', headers: {'priority': '9'}});

        // this.stompClient.publish({destination: '/app/hello', body: JSON.stringify(message)});
    }

    onMessageReceived(message) {
        console.log("Message Recieved from Server 0 :: " + message);
        console.log("Message Recieved from Server 1 :: " + message.body);
        this.appComponent.handleMessage(JSON.parse(message.body).content);
    }
}