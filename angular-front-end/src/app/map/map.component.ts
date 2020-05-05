import { Component, OnInit } from '@angular/core';
/* Imports */
import * as am4core from "@amcharts/amcharts4/core";
import * as am4maps from "@amcharts/amcharts4/maps";
import am4geodata_worldLow from "@amcharts/amcharts4-geodata/worldLow";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import { HomeComponent } from '../home/home.component';

am4core.useTheme(am4themes_animated);
/* Chart code */
// Themes begin
am4core.useTheme(am4themes_animated);
// Themes end

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  country:string;
  set = new Set();
  countries = ["ar", "au", "at", "be", "br", "bg", "ca", "cn", "co", "cu", 
  "cz", "eg", "fr", "de", "gr", "hk", "hu", "in", "id", "ie", "il", "it", "jp", 
  "lv", "lt", "my", "mx", "ma", "nl", "nz", "ng", "no", "ph", "pl", "pt", "ro", 
  "ru", "sa", "rs", "sg", "sk", "si", "za", "kr", "se", "ch", "tw", "th", "tr", 
  "ae", "ua", "gb", "us", "ve" ];
  loading = true;

  constructor(private home:HomeComponent) { }

  ngOnInit(): void {

    for (let str of this.countries) this.set.add(str);
    
    // Create map instance
    var chart = am4core.create("chartdiv", am4maps.MapChart);

    // Set map definition
    chart.geodata = am4geodata_worldLow;

    // Set projection
    chart.projection = new am4maps.projections.Miller();

    // Create map polygon series
    var polygonSeries = chart.series.push(new am4maps.MapPolygonSeries());

    // Exclude Antartica
    polygonSeries.exclude = ["AQ"];

    // Make map load polygon (like country names) data from GeoJSON
    polygonSeries.useGeodata = true;

    // Configure series
    var polygonTemplate = polygonSeries.mapPolygons.template;
    polygonTemplate.tooltipText = "{name}";
    polygonTemplate.fill = am4core.color("#74B266");

    // Create hover state and set alternative fill color
    var hs = polygonTemplate.states.create("hover");
    hs.properties.fill = am4core.color("#367B25");
    var self = this;

    // Add hit events
    polygonSeries.mapPolygons.template.events.on("hit", function(ev) {
      let val: any;
      val = ev.target.dataItem.dataContext;
      self.getCountryNews(val.id);
    });

    setTimeout(()=>{
      this.loading = false;}, 500);
  }

  private getCountryNews(country: string) {
    country = country.toLowerCase();
    if (!this.set.has(country)) return;
    this.home.changeMode('news');
    this.home.reset(country);
  }
}