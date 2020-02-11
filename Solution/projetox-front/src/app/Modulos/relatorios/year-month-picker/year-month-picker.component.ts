import { Component, OnInit, Output, EventEmitter } from "@angular/core";
import * as moment from "moment";
import { FormControl } from "@angular/forms";
import { MatDatepicker, DateAdapter, MAT_DATE_LOCALE, MAT_DATE_FORMATS } from "@angular/material";
import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from "@angular/material-moment-adapter";

export const MY_FORMATS = {
    parse: {
        dateInput: "MM/YYYY"
    },
    display: {
        dateInput: "MM/YYYY",
        monthYearLabel: "MMM YYYY",
        dateA11yLabel: "LL",
        monthYearA11yLabel: "MMMM YYYY"
    }
};

@Component({
    selector: "app-year-month-picker",
    templateUrl: "./year-month-picker.component.html",
    styleUrls: ["./year-month-picker.component.css"],
    providers: [
        // `MomentDateAdapter` can be automatically provided by importing `MomentDateModule` in your
        // application's root module. We provide it at the component level here, due to limitations of
        // our example generation script.
        {
            provide: DateAdapter,
            useClass: MomentDateAdapter,
            deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
        },

        { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS }
    ]
})
export class YearMonthPickerComponent implements OnInit {
    constructor() {}
    @Output() mes : EventEmitter<any> = new EventEmitter();
    @Output() ano : EventEmitter<any> = new EventEmitter();
    ngOnInit() {

        
    }
    // tslint:disable-next-line: member-ordering
    date = new FormControl(moment());

    chosenYearHandler(normalizedYear: moment.Moment) {
        this.ano.emit(normalizedYear.year());
        const ctrlValue = this.date.value;
        ctrlValue.year(normalizedYear.year());
        this.date.setValue(ctrlValue);
    }

    chosenMonthHandler(
        normalizedMonth: moment.Moment,
        datepicker: MatDatepicker<moment.Moment>
    ) {
        this.mes.emit(normalizedMonth.month());
        const ctrlValue = this.date.value;
        ctrlValue.month(normalizedMonth.month());
        this.date.setValue(ctrlValue);
        datepicker.close();
        
    }
}
