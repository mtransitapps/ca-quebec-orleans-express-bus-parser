package org.mtransit.parser.ca_quebec_orleans_express_bus;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MSpec;
import org.mtransit.parser.mt.data.MTrip;

// http://gtfs.keolis.ca/gtfs.zip
public class QuebecOrleansExpressBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-quebec-orleans-express-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new QuebecOrleansExpressBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;

	@Override
	public void start(String[] args) {
		System.out.printf("Generating Orleans Express bus data...\n");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("Generating Orleans Express bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public String getRouteLongName(GRoute gRoute) {
		String routeLongName = gRoute.route_long_name;
		routeLongName = MSpec.SAINT.matcher(routeLongName).replaceAll(MSpec.SAINT_REPLACEMENT);
		routeLongName = MSpec.CLEAN_PARENTHESE1.matcher(routeLongName).replaceAll(MSpec.CLEAN_PARENTHESE1_REPLACEMENT);
		routeLongName = MSpec.CLEAN_PARENTHESE2.matcher(routeLongName).replaceAll(MSpec.CLEAN_PARENTHESE2_REPLACEMENT);
		return MSpec.cleanLabel(routeLongName);
	}

	@Override
	public String getRouteShortName(GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.route_short_name)) {
			return "ORL EXP"; // TODO better route short names
		}
		return super.getRouteShortName(gRoute);
	}

	private static final String AGENCY_COLOR = "01ADB9";

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Override
	public String getRouteColor(GRoute gRoute) {
		if ("2".equals(gRoute.route_id)) return "2B80A5";
		if ("4".equals(gRoute.route_id)) return "2B80A5";
		if ("6".equals(gRoute.route_id)) return "EB6B24";
		if ("8".equals(gRoute.route_id)) return "75B442";
		if ("9".equals(gRoute.route_id)) return "75B442";
		if ("11".equals(gRoute.route_id)) return "0F807C";
		if ("122".equals(gRoute.route_id)) return "ED253F";
		if ("123".equals(gRoute.route_id)) return "ED253F";
		return super.getRouteColor(gRoute);
	}

	private static final int INBOUND = 0;

	private static final int OUTBOUND = 1;

	@Override
	public void setTripHeadsign(MRoute route, MTrip mTrip, GTrip gTrip) {
		String stationName = cleanTripHeadsign(gTrip.trip_headsign);
		int directionId;
		int routeId = Integer.parseInt(gTrip.getRouteId());
		switch (routeId) {
		case 2:
			if (gTrip.trip_headsign.contains("Trudeau")) {
				stationName = "Aéroport-Trudeau";
				directionId = OUTBOUND;
			} else {
				directionId = INBOUND;
			}
			break;
		case 4:
			if ("Québec".equals(gTrip.trip_headsign)) {
				directionId = OUTBOUND;
			} else {
				directionId = INBOUND;
			}
			break;
		case 6:
			if ("Québec".equals(gTrip.trip_headsign)) {
				directionId = OUTBOUND;
			} else {
				directionId = INBOUND;
			}
			break;
		case 8:
			if ("Rimouski".equals(gTrip.trip_headsign)) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 9:
			if ("Rimouski".equals(gTrip.trip_headsign)) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 11:
			if ("Montréal".equals(gTrip.trip_headsign)) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 122:
			if ("Montréal".equals(gTrip.trip_headsign)) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 123:
			if ("Québec".equals(gTrip.trip_headsign)) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		default:
			directionId = -1;
			System.out.println("Unexpected route ID " + routeId + "!");
			System.exit(-1);
		}
		mTrip.setHeadsignString(stationName, directionId);
	}

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		return MSpec.cleanLabelFR(tripHeadsign);
	}

	@Override
	public String cleanStopName(String gStopName) {
		return super.cleanStopNameFR(gStopName);
	}
}
