/** Create a chart and adds it to the page. */
function drawChart() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Hours per Day');
  data.addColumn('number', 'Count');
        data.addRows([
          ['Coding',     11],
          ['Cook',      2],
          ['Eat',      1],
          ['Entertain', 2],
          ['Sleep',    8]
        ]);

  const options = {
    'title': 'My Daily Activities',
    'width': 500,
    'height': 300,
    'colors': ['#ef8f80', '#e6693e', '#ec8f6e', '#f3b49f', '#f6c7b6'],
    'backgroundColor': 'transparent',
    'legend': {textStyle: {color: '#ffffff'}},
    'titleTextStyle': {color: '#ffffff'},
    'is3D': true
  };

  const chart = new google.visualization.PieChart(document.getElementById('chart-container'));
  chart.draw(data, options);
}
