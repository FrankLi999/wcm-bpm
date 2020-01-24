import {AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import { mxgraph } from 'mxgraph';
declare var require: any;
export var mx: typeof mxgraph = require('mxgraph')({
	mxBasePath: 'assets/mxgraph'
  });

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {


  @ViewChild('graphContainer') graphContainer: ElementRef;

  ngAfterViewInit() {
    // const graph = new mx.mxGraph(this.graphContainer.nativeElement);

    // try {
    //   const parent = graph.getDefaultParent();
    //   graph.getModel().beginUpdate();

    //   const vertex1 = graph.insertVertex(parent, '1', 'Vertex 1', 0, 0, 200, 80);
    //   const vertex2 = graph.insertVertex(parent, '2', 'Vertex 2', 0, 0, 200, 80);

    //   graph.insertEdge(parent, '', '', vertex1, vertex2);
    // } finally {
    //   graph.getModel().endUpdate();
    //   new mx.mxHierarchicalLayout(graph).execute(graph.getDefaultParent());
	// }
	this.createEditor('assets/config/workfloweditor.xml');
  }

  createEditor(config) {
		console.log('mxgraph', mxgraph, mx)
		
		mx.mxGraph.prototype.htmlLabels = true;
	
		mx.mxGraph.prototype.isWrapping = function(cell)
		{
			return true;
		};
		
		mx.mxConstants.DEFAULT_HOTSPOT = 1;
		
		// Enables guides
		mx.mxGraphHandler.guidesEnabled = true;
	    // Alt disables guides
	    mx.mxGuide.prototype.isEnabledForEvent = function(evt)
	    {
	    	return !mx.mxEvent.isAltDown(evt);
	    };
		
		// Enables snapping waypoints to terminals
		// mx.mxEdgeHandler.prototype.snapToTerminals = true;
		mx.mxEdgeHandler.snapToTerminals = true;
		window.onbeforeunload = function() { return mx.mxResources.get('changesLost'); };


		var editor = null;
		
		var hideSplash = function() {
			// Fades-out the splash screen
			var splash = document.getElementById('splash');
			
			if (splash != null) {
				try {
					mx.mxEvent.release(splash);
					mx.mxEffects.fadeOut(splash, 100, true, 0, 0, true);
				} catch (e) {
					splash.parentNode.removeChild(splash);
				}
			}
		};
		
		try {
			if (!mx.mxClient.isBrowserSupported()) {
				mx.mxUtils.error('Browser is not supported!', 200, false);
			} else {
				mx.mxObjectCodec.allowEval = true;
				var node = mx.mxUtils.load(config).getDocumentElement();
				editor = new mx.mxEditor(node);
				mx.mxObjectCodec.allowEval = false;
				
				// Adds active border for panning inside the container
				editor.graph.createPanningManager = function()
				{
					var pm = new mx.mxPanningManager(this);
					pm.border = 30;
					
					return pm;
				};
				
				editor.graph.allowAutoPanning = true;
				editor.graph.timerAutoScroll = true;
				
				// Updates the window title after opening new files
				var title = document.title;
				var funct = function(sender)
				{
					document.title = title + ' - ' + sender.getTitle();
				};
				
				editor.addListener(mx.mxEvent.OPEN, funct);
				
				// Prints the current root in the window title if the
				// current root of the graph changes (drilling).
				editor.addListener(mx.mxEvent.ROOT, funct);
				funct(editor);
				
				// Displays version in statusbar
				editor.setStatus('mxGraph '+ mx.mxClient.VERSION);

				// Shows the application
				hideSplash();
			}
		} catch (e) {
			hideSplash();
			// Shows an error message if the editor cannot start
			mx.mxUtils.alert('Cannot start application: ' + e.message);
			throw e; // for debugging
		}

		return editor;
	}
}
