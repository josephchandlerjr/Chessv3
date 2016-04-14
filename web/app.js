var Chess =  {
	fromRow : undefined,
	fromCol : undefined,
	toRow : undefined,
        toCol : undefined,
	logTo : function(event){
		event.preventDefault;
		Chess.toRow = $(this).data('row');
		Chess.toCol = $(this).data('col');
		alert('from '+ 
		      Chess.fromRow+' '+ 
		      Chess.fromCol+' to '+ 
		      Chess.toRow+' '+ 
		      Chess.toCol);
		Chess.makeMove();
	},

	makeMove :  function(fromRow,fromCol,toRow,toCol){
		//ajax call here to takeAction;
	},
	
	logFrom : function(event){
		event.preventDefault;
		Chess.fromRow = $(this).data('row');
		Chess.fromCol = $(this).data('col');

	},
	init : function() {
		var white = 'board-white-square';
		var black = 'board-black-square';
		var color = white;

		for(var i=0 ; i < 8; i++){
			color = color == white ? black : white; 
			for(var j=0 ; j < 8; j++){
				var square = $('<div data-row='+i+' data-col='+j+'></div>');
				square.addClass(color);
				// listeners for moves
				square.on('mousedown',Chess.logFrom);
				square.on('mouseup',Chess.logTo);
				$('.chess-board').append(square);
				color = color == white ? black : white; 
			}
		}
	},
	updateBoard : function(){
		$.ajax('/Chess/game.do',{
		type: 'GET',
		data: {"fromRow":Chess.fromRow,
		        "fromCol":Chess.fromCol,
			"toRow":Chess.toRow,
			"toCol":Chess.toCol},
		success: function(){
			alert('updated');}
		});
		
	},
	makeMove : function(){
		$.ajax('/Chess/game.do',{
		type: 'POST',
		data: {"fromRow":Chess.fromRow,
		        "fromCol":Chess.fromCol,
			"toRow":Chess.toRow,
			"toCol":Chess.toCol},
		success: function(){
			alert('move requested');}
		});
		Chess.updateBoard();
	},
	removePiece : function(el){
		var classList = el.attr('class').split(/\s+/);
		$.each(classList, function(index,item){
			if(item.startsWith('piece')){
				el.removeClass(item);
				return;
			}
		});
	}
}


$(document).ready(function(){
	Chess.init();
	$('.play-chess').on('click',function(event){
		event.preventDefault();
		$('.chess-board').toggleClass('is-visible');
		});

	$('button.new-game').on('click',function(event){
		event.preventDefault();
		alert('click me long time');});

});

