/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

                    var size = 120,
                            newsContent = $('.wrapperRow'),
                            newsText = newsContent.text();

                    if (newsText.length > size) {
                        newsContent.text(newsText.slice(0, size) + ' ...');
                    }

              