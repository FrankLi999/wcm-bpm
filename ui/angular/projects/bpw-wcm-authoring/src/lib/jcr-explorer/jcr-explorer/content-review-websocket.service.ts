import { WebSocketService } from '../../service/websocket.service';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
export class ContentWebSocketService extends WebSocketService {
    uiComponent: any;
    constructor(uiComponent: any) {
        super();
        this.uiComponent = uiComponent;
    }
    _connect() {
        console.log("Initialize WebSocket Connection");
        let ws = new SockJS(this.webSocketEndPoint);
        this.stompClient = Stomp.over(ws);
        const _this = this;
        this.stompClient.connect({}, function (frame) {
            
            _this.stompClient.subscribe(_this.topic, function (message) {
                console.log("Message Recieved from Server 0 :: " + message);
                _this.onMessageReceived(message);
            });
            //_this.stompClient.reconnect_delay = 2000;
            _this._send("/wcm-app/test", {name: 'test jcr message'});
        }, this.errorCallBack);
        
    };
    onMessageReceived(message) {        
        console.log("Message Recieved from Server 1 :: " + message.body);
        this.uiComponent.handleJcrMessage(JSON.parse(message.body).content);
    }
}