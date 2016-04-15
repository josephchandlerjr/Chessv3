var Chess =  {
	fromRow : undefined,
	fromCol : undefined,
	toRow : undefined,
        toCol : undefined,
	squares : [],
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
				this.squares.push(square);
				color = color == white ? black : white; 
			}
		}
		this.updateBoard();
	},
	updateBoard : function(){
		var obj = this;
		$.ajax('/Chess/game.do',{
		type: 'GET',
		success: function(result){
			var pieces = result.split(" ");
			for(var i=0; i<obj.squares.length; i++){
				//var col = i % 8;
				//var row = Math.floor(i / 8);
				//console.log(row == obj.squares[i].data('row'));
				obj.removePiece(obj.squares[i]);
				obj.addPiece(obj.squares[i],pieces[i]);
			}
		});
		
	},
	makeMove : function(){
		$.ajax('/Chess/game.do',{
		type: 'POST',
		data: {"fromRow":Chess.fromRow,
		        "fromCol":Chess.fromCol,
			"toRow":Chess.toRow,
			"toCol":Chess.toCol},
		success: function(result){
			alert('move requested');}
		});
		Chess.updateBoard();
	},
	/* removePiece takes a DOM element to remove image from */
	removePiece : function(el){
		var classList = el.attr('class').split(/\s+/);
		$.each(classList, function(index,item){
			if(item.startsWith('piece')){
				el.removeClass(item);
				return;
			}
		});
	},
	/* addPiece takes a DOM element to add image to, and class name */
	addPiece : function(el,name){
		el.addClass("piece-"+name);
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

