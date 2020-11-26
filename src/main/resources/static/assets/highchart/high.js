/**
 * 
 */

function column(data,cat,title){
	Highcharts.chart('container', {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Sample Player report.xlsx'
	    },
	    xAxis: {
	        categories: cat,
	        crosshair: true
	    },
	    legend: {
	        align: 'right',
	        verticalAlign: 'top',
	        layout: 'vertical',
	        x: 10,
	        y: 120
	    },
	    yAxis: {
	        min: 0,
	        title: {
	            text: 'Rating'
	        }
	    },
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	    },
	    plotOptions: {
	        column: {
	            pointPadding: 0.2,
	            borderWidth: 0
	        }
	    },
	    series: data
	
	});
}

function bpiAnalysis(data,cat,div,title){
	Highcharts.chart(div, {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Sample Player report.xlsx'
	    },
	    xAxis: {
	        categories: cat,
	        crosshair: true
	    },
	    legend: {
	        align: 'right',
	        verticalAlign: 'top',
	        layout: 'vertical',
	        x: 10,
	        y: 120
	    },
	    yAxis: {
	        min: 0,
	        title: {
	            text: 'Rating'
	        }
	    },
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true,
	        positioner: function(labelWidth, labelHeight, point) {         
                var tooltipX, tooltipY;
                   if (point.plotX + labelWidth > this.chart.plotWidth) {
                       tooltipX = point.plotX + this.chart.plotLeft - labelWidth - 40;
                   } else {
                       tooltipX = point.plotX + this.chart.plotLeft - 100;
                   }
                   tooltipY = point.plotY + this.chart.plotTop - 200;
                   return {
                       x: tooltipX,
                       y: tooltipY
                   };       
           }
	    },
	    plotOptions: {
	        column: {
	            pointPadding: 0.2,
	            borderWidth: 0
	        },
	        series: {
	            dataLabels: {
	                enabled: true
	            }
	        }
	    },
	    
	    series: data
	
	});
}

/*
 * For food oder graph 
 * 
 */
function column1(data,cat,title){
	console.log(data)
	Highcharts.chart('container', {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Food Order report.xlsx'
	    },
	    xAxis: {
	    	type:'category'
	    },
	    legend: {
	        align: 'right',
	        verticalAlign: 'top',
	        layout: 'vertical',
	        x: 10,
	        y: 120
	    },
	    yAxis: {
	        min: 0,
	        title: {
	            text: 'total no. of order'
	        }
	    },
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{series.name}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{point.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	    },
	    plotOptions: {
	        column: {
	            pointPadding: 0.2,
	            borderWidth: 0
	        }
	    },
	    series: [{
	    	name:"no. of order",
	    	colorByPoint:true,
	    	data:data
	    }]
	
	});
}


/*
 * For OCCUPANCY REPORT GRAPH
 * 
 */
function column2(data,cat,title){
console.log("JS: ",data)

	Highcharts.chart('container', {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Purchase Order'
	    },
	    xAxis: {
	    	type:'category'
	    		 
	    },
	    legend: {
	        align: 'right',
	        verticalAlign: 'top',
	        layout: 'vertical',
	        x: 10,
	        y: 100
	    },
	    yAxis: { 
	    	 allowDecimals: false,
		       
	        min: 0,
	        title: {
	            text: 'No. Of Order'
	        }
	    },
	    
	    
	 
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:1f}</b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	    },
	    
	    plotOptions: {
	        column: {
	            pointPadding: 0.3,
	            borderWidth: 0
	        }
	    },
	    series: [{
	    	name:"Order",
	    	colorByPoint:true,
	    	data:[
                {
                    name: "Apr",
                    y: 4,
                },
                {
                	name: "May",
                	y: 5,
                },
                {
                    name: "Jun",
                    y: 3,
                },
                {
                    name: "Jul",
                    y: 4,
                },
                {
                    name: "Aug",
                    y: 1,
                },
                {
                    name: "Sep",
                    y: 2,
                },
                {
                    name: "Oct",
                    y: 3,
                },
                {
                    name: "Nov",
                    y: 1,
                }
            ]
	    }]
	
	});
}

function columnMLeave(data,cat,title){
	console.log("JS: ",data)
	
	Highcharts.chart('container', {
		chart: {
			type: 'column',
			marginRight: 120
		},
		title: {
			text: title
		},
		subtitle: {
			text: 'Source: Employee Leave'
		},
		xAxis: {
			type:'category'
				
		},
		legend: {
			align: 'right',
			verticalAlign: 'top',
			layout: 'vertical',
			x: 10,
			y: 100
		},
		yAxis: { 
			allowDecimals: false,
			
			min: 0,
			title: {
				text: 'No. Of Leave'
			}
		},
		
		
		
		tooltip: {
			headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
			pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
			'<td style="padding:0"><b>{point.y:1f}</b></td></tr>',
			footerFormat: '</table>',
			shared: true,
			useHTML: true
		},
		
		plotOptions: {
			column: {
				pointPadding: 0.3,
				borderWidth: 0
			}
		},
		series: [{
			name:"Leave",
			colorByPoint:true,
			data:[
				{
					name: "Apr",
					y: 4,
				},
				{
					name: "May",
					y: 5,
				},
				{
					name: "Jun",
					y: 3,
				},
				{
					name: "Jul",
					y: 4,
				},
				{
					name: "Aug",
					y: 1,
				},
				{
					name: "Sep",
					y: 2,
				},
				{
					name: "Oct",
					y: 3,
				},
				{
					name: "Nov",
					y: 1,
				}
				]
		}]
		
	});
}



/*
 * For REVENUE REPORT GRAPH
 * 
 */
function column3(data,cat,title){
	console.log(data)
	
	Highcharts.chart('container1', {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Payment Voucher'
	    },
	    xAxis: {
	    	type:'category'
	    		 
	    },
	    legend: {
	        align: 'right',
	        verticalAlign: 'top',
	        layout: 'vertical',
	        x: 10,
	        y: 100
	    },
	    yAxis: { 
	   		       
	       
	        title: {
	            text: 'Purchase in thousand'
	        }
	    },
	    
	    
	 
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>₹ {point.y:.2f}</b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	        
	    },
	    
	    plotOptions: {
	        column: {
	            pointPadding: 0.3,
	            borderWidth: 0
	        }
	    },
	    series: [{
	    	name:"Purchase",
	    	colorByPoint:true,
	    	data:[
                {
                    name: "Apr",
                    y: 4000,
                },
                {
                	name: "May",
                	y: 5000,
                },
                {
                    name: "Jun",
                    y: 3000,
                },
                {
                    name: "Jul",
                    y: 4000,
                },
                {
                    name: "Aug",
                    y: 1000,
                },
                {
                    name: "Sep",
                    y: 2000,
                },
                {
                    name: "Oct",
                    y: 3000,
                },
                {
                    name: "Nov",
                    y: 1000,
                }
            ]
	    }]
	
	});
}

/*
 * For OCCUPANCY REPORT GRAPH
 * 
 */
function column4(data,cat,title){

	Highcharts.chart('container', {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Laundry Report.xlsx'
	    },
	    xAxis: {
	    	type:'Month'
	    		 
	    },
	    legend: {
	        align: 'right',
	        verticalAlign: 'top',
	        layout: 'vertical',
	        x: 10,
	        y: 100
	    },
	    yAxis: { 
	    	 allowDecimals: false,
		       
	        min: 0,
	        title: {
	            text: 'no. of orders'
	        }
	    },
	    
	    
	 
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:1f} order</b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	    },
	    
	    plotOptions: {
	        column: {
	            pointPadding: 0.3,
	            borderWidth: 0
	        }
	    },
	    series: [{
	    	name:"Orders",
	    	colorByPoint:true,
	    	data:data
	    }]
	
	});
}

function piechart(data) {
	Highcharts.chart('container2', {
		  chart: {
		    plotBackgroundColor: null,
		    plotBorderWidth: null,
		    plotShadow: false,
		    type: 'pie'
		  },
		  title: {
			  text: 'Month VS Order'
		    //text: 'Browser market shares in January, 2018'
		  },
		  tooltip: {
		    pointFormat: '{series.name} <br> <b>Order: {point.y}</b>'
		  },
		  plotOptions: {
		    pie: {
		      allowPointSelect: true,
		      cursor: 'pointer',
		      dataLabels: {
		        enabled: true,
		        format: '<b>{point.name}</b> <br>Order: {point.y} '
		      }
		    }
		  },
		  series: [{
		    name: 'Order',
		    colorByPoint: true,
		    data: data
		  }]
		});
}
Highcharts.setOptions({
	  colors: Highcharts.map(Highcharts.getOptions().colors, function (color) {
	    return {
	      radialGradient: {
	        cx: 0.5,
	        cy: 0.3,
	        r: 0.7
	      },
	      stops: [
	        [0, color],
	        [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
	      ]
	    };
	  })
	});
function piechart1(data) {
	// Radialize the colors
	
	
	Highcharts.chart('container3', {
		chart: {
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			type: 'pie'
		},
		title: {
			text: 'Month VS Purchase'
				//text: 'Browser market shares in January, 2018'
		},
		tooltip: {
			pointFormat: '{series.name} <br> <b>Sales: ₹ {point.y:.2f}</b>'
		},
		plotOptions: {
			pie: {
			      allowPointSelect: true,
			      cursor: 'pointer',
			      dataLabels: {
			        enabled: false
			      },
			      showInLegend: true
			    }
			/*pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true,
					format: '<b>{point.name}</b> <br>Sales: ₹ {point.y:.2f} '
				}
			}*/
		},
		series: [{
			name: 'Pie Chart',
			colorByPoint: true,
			data: data
		}]
	});
}

