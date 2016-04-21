// ---------------------------------------------------
// Modal Chess
// ---------------------------------------------------
var Chess =  {
	fromRow : undefined,
	fromCol : undefined,
	toRow : undefined,
        toCol : undefined,
	squares : [],
	logTo : function(event){
		event.preventDefault();
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
		event.preventDefault();
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
				square.addClass('board-square');
				//square.addClass('board-square');
				// listeners for moves
				square.on('mousedown',this.logFrom);
				square.on('mouseup',this.logTo);
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
			var blackLabel = $('.black-label');
			var whiteLabel = $('.white-label');
			if(result["whiteToMove"]){
				whiteLabel.text("your move");
				blackLabel.text("waiting");
			}
			if(result["blackToMove"]){
				blackLabel.text("your move");
				whiteLabel.text("waiting");
			}
			if(result["whiteInCheck"])
				whiteLabel.text("In Check!");
			if(result["blackInCheck"])
				blackLabel.text("In Check!");
			if(result["whiteHasWon"]){
				whiteLabel.text("You Win!");
				blackLabel.text("You Lose :( ");
			}
			if(result["blackHasWon"]){
				blackLabel.text("You Win!");
				whiteLabel.text("You Lose :( ");
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

// ---------------------------------------------------
// Document Ready
// ---------------------------------------------------

$(document).ready(function(){
	Chess.init();
	$('.play-chess').on('click',function(event){
		event.preventDefault();
		$('.chess-board').addClass('is-visible');
		$('.modal-overlay').addClass('is-visible');
		});
	$('.modal-overlay').on('click',function(event){
		$('.chess-board').removeClass('is-visible');
		$('.modal-overlay').removeClass('is-visible');
	});

	$('button.new-game').on('click',function(event){
		event.preventDefault();
		console.log('click me long time');});

});

