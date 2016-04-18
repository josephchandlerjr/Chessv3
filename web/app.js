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
		console.log('from '+ 
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
		console.log('about to update board');
		var self = this;
		$.ajax('/Chess/game.do',{
		type: 'GET',
		dataType: 'json',
		success: function(result){
			console.log('here in the success function of updateBoard');
			console.log(result);
			var pieces = result["boardRepr"].split(" ");
			for(var i=0; i<self.squares.length; i++){
				self.removePiece(self.squares[i]);
				self.addPiece(self.squares[i],pieces[i]);
			}
		}});
	},
	makeMove : function(){
		self = this;
		$.ajax('/Chess/game.do',{
		type: 'POST',
		data: {"fromRow":self.fromRow,
		        "fromCol":self.fromCol,
			"toRow":self.toRow,
			"toCol":self.toCol},
		success: function(){
			self.updateBoard();
		}
		});
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
		console.log('click me long time');});

});

