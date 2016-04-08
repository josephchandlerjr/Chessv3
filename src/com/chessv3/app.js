
$(document).ready(function(){
			var chessBoard = $('.chess-board');
			var white = 'board-white-square';
			var black = 'board-black-square';
			var color = white;
			var fromRow = null;
			var fromCol = null;
			var toRow = null;
			var toCol = null;
			for(var i=0 ; i < 8; i++){
				color = color== white ? black : white; 
				for(var j=0 ; j < 8; j++){
				var square = $('<div data-row='+i+' data-col='+j+'></div>');
				square.addClass(color);
				// listeners for moves
				square.on('mousedown',function(){
					fromRow = $(this).data('row');
					fromCol = $(this).data('col');
				});
				square.on('mouseup',function(){
					toRow = $(this).data('row');
					toCol = $(this).data('col');
					$.ajax('/moveRequest.do',{
						success: function(response){
							alert('move request');
						}
						});
					alert('from '+fromRow+' '+fromCol+' to '+toRow+' '+toCol);
				});
				chessBoard.append(square);
				color = color== white ? black : white; 
				}
			}
			$('.play-chess').on('click',function(event){
				event.preventDefault();
				$('.chess-board').toggleClass('is-visible');
			});
			$('button.new-game').on('click',function(event){
				event.preventDefault();
				alert('click me long time');});
		});

