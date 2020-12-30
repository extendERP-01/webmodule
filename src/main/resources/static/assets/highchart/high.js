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
	        pointFormat: '<tr><td style="color:{series.color};padding:0">No. of Order : </td>' +
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
	    	name:"",
	    	colorByPoint:true,
	    	data:[
                {
                    name: "Apr",
                    y: 4,
                    color: '#A9A9A9'
                },
                {
                	name: "May",
                	y: 5,
                	color: '#A9A9A9'
                },
                {
                    name: "Jun",
                    y: 3,
                    color: '#A9A9A9'
                },
                {
                    name: "Jul",
                    y: 4,
                    color: '#A9A9A9'
                },
                {
                    name: "Aug",
                    y: 1,
                    color: '#A9A9A9'
                },
                {
                    name: "Sep",
                    y: 2,
                    color: '#A9A9A9'
                },
                {
                    name: "Oct",
                    y: 3,
                    color: '#A9A9A9'
                },
                {
                    name: "Nov",
                    y: 1,
                    color: '#A9A9A9'
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
					color: '#A9A9A9'
				},
				{
					name: "May",
					y: 5,
					color: '#A9A9A9'
				},
				{
					name: "Jun",
					y: 3,
					color: '#A9A9A9'
				},
				{
					name: "Jul",
					y: 4,
					color: '#A9A9A9'
				},
				{
					name: "Aug",
					y: 1,
					color: '#A9A9A9'
				},
				{
					name: "Sep",
					y: 2,
					color: '#A9A9A9'
				},
				{
					name: "Oct",
					y: 3,
					color: '#A9A9A9'
				},
				{
					name: "Nov",
					y: 1,
					color: '#A9A9A9'
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

	    title: {
	        text: 'Solar Employment Growth by Sector, 2010-2016'
	    },

	    subtitle: {
	        text: 'Source: thesolarfoundation.com'
	    },

	    yAxis: {
	        title: {
	            text: 'Number of Employees'
	        }
	    },

	    xAxis: {
	        accessibility: {
	            rangeDescription: 'Range: 2010 to 2017'
	        }
	    },

	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },

	    plotOptions: {
	        series: {
	            label: {
	                connectorAllowed: false
	            },
	            pointStart: 2010
	        }
	    },

	    series: [{
	        name: 'Installation',
	        data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
	    }, {
	        name: 'Manufacturing',
	        data: [24916, 24064, 29742, 29851, 32490, 30282, 38121, 40434]
	    }, {
	        name: 'Sales & Distribution',
	        data: [11744, 17722, 16005, 19771, 20185, 24377, 32147, 39387]
	    }, {
	        name: 'Project Development',
	        data: [null, null, 7988, 12169, 15112, 22452, 34400, 34227]
	    }, {
	        name: 'Other',
	        data: [12908, 5948, 8105, 11248, 8989, 11816, 18274, 18111]
	    }],

	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }

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



/*
 * For REVENUE REPORT GRAPH
 * 
 */
function lineGrapg(data,cat,titletxt){  
	Highcharts.chart('container1', { 
	    yAxis: {
	        title: {
	            text: titletxt
	        }
	    },

	    xAxis: {
	        categories: cat
	      },

	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	    title: {
	        text: null
	    },
	    plotOptions: {
	        series: {
	            label: {
	                connectorAllowed: false
	            },
	            pointStart: 01
	        }
	    },

	    series:  data,
	    
	    

	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }

	});
}

function downloadChartPdf(){
	 Highcharts.charts[0].exportChart({
		    type: 'application/pdf'
		  });
}
