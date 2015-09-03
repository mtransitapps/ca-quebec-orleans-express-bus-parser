package org.mtransit.parser.ca_quebec_orleans_express_bus;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.CleanUtils;
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
		System.out.printf("\nGenerating Orleans Express bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("\nGenerating Orleans Express bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
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
		String routeLongName = gRoute.getRouteLongName();
		routeLongName = CleanUtils.SAINT.matcher(routeLongName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESE1.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESE1_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESE2.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESE2_REPLACEMENT);
		return CleanUtils.cleanLabel(routeLongName);
	}

	private static final String RID_123 = "123";
	private static final String RID_122 = "122";
	private static final String RID_11 = "11";
	private static final String RID_9 = "9";
	private static final String RID_8 = "8";
	private static final String RID_6 = "6";
	private static final String RID_4 = "4";
	private static final String RID_2 = "2";

	private static final String RSN_123 = "QC RK";
	private static final String RSN_122 = "MT RK E";
	private static final String RSN_11 = "VT MT";
	private static final String RSN_9 = "RK GR S";
	private static final String RSN_8 = "RK GS N";
	private static final String RSN_6 = "MT QC N";
	private static final String RSN_4 = "MT QC S";
	private static final String RSN_2 = "MT YUL";

	@Override
	public String getRouteShortName(GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteShortName())) {
			if (RID_2.equals(gRoute.getRouteId())) {
				return RSN_2; // Montréal - Aéroport Montréal-Trudeau
			} else if (RID_4.equals(gRoute.getRouteId())) {
				return RSN_4; // Montréal - Québec ( Rive-sud, Express )
			} else if (RID_6.equals(gRoute.getRouteId())) {
				return RSN_6; // Montréal - Trois-Rivières - Québec ( Rive Nord )
			} else if (RID_8.equals(gRoute.getRouteId())) {
				return RSN_8; // Rimouski - Matane - Gaspé ( Gaspésie Nord )
			} else if (RID_9.equals(gRoute.getRouteId())) {
				return RSN_9; // Rimouski - Grande-Rivière (Gaspésie Sud)
			} else if (RID_11.equals(gRoute.getRouteId())) {
				return RSN_11; // Victoriaville - Montréal
			} else if (RID_122.equals(gRoute.getRouteId())) {
				return RSN_122; // 'Montréal - Rimouski (Express)
			} else if (RID_123.equals(gRoute.getRouteId())) {
				return RSN_123; // Québec - Rimouski (Bas-St-Laurent)
			}
		}
		return super.getRouteShortName(gRoute);
	}

	private static final String AGENCY_COLOR = "01ADB9";

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	private static final String COLOR_ED253F = "ED253F";
	private static final String COLOR_0F807C = "0F807C";
	private static final String COLOR_75B442 = "75B442";
	private static final String COLOR_EB6B24 = "EB6B24";
	private static final String COLOR_2B80A5 = "2B80A5";

	@Override
	public String getRouteColor(GRoute gRoute) {
		if (RID_2.equals(gRoute.getRouteId())) return COLOR_2B80A5;
		if (RID_4.equals(gRoute.getRouteId())) return COLOR_2B80A5;
		if (RID_6.equals(gRoute.getRouteId())) return COLOR_EB6B24;
		if (RID_8.equals(gRoute.getRouteId())) return COLOR_75B442;
		if (RID_9.equals(gRoute.getRouteId())) return COLOR_75B442;
		if (RID_11.equals(gRoute.getRouteId())) return COLOR_0F807C;
		if (RID_122.equals(gRoute.getRouteId())) return COLOR_ED253F;
		if (RID_123.equals(gRoute.getRouteId())) return COLOR_ED253F;
		return super.getRouteColor(gRoute);
	}

	private static final String MONTRÉAL = "Montréal";
	private static final String RIMOUSKI = "Rimouski";
	private static final String QUÉBEC = "Québec";
	private static final String AÉROPORT_TRUDEAU = "Aéroport-Trudeau";
	private static final String TRUDEAU = "Trudeau";

	private static final int INBOUND = 0;

	private static final int OUTBOUND = 1;

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		String stationName = cleanTripHeadsign(gTrip.getTripHeadsign());
		int directionId;
		int routeId = Integer.parseInt(gTrip.getRouteId());
		switch (routeId) {
		case 2:
			if (gTrip.getTripHeadsign().contains(TRUDEAU)) {
				stationName = AÉROPORT_TRUDEAU;
				directionId = OUTBOUND;
			} else {
				directionId = INBOUND;
			}
			break;
		case 4:
			if (QUÉBEC.equals(gTrip.getTripHeadsign())) {
				directionId = OUTBOUND;
			} else {
				directionId = INBOUND;
			}
			break;
		case 6:
			if (QUÉBEC.equals(gTrip.getTripHeadsign())) {
				directionId = OUTBOUND;
			} else {
				directionId = INBOUND;
			}
			break;
		case 8:
			if (RIMOUSKI.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 9:
			if (RIMOUSKI.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 11:
			if (MONTRÉAL.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 122:
			if (MONTRÉAL.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 123:
			if (QUÉBEC.equals(gTrip.getTripHeadsign())) {
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
		return CleanUtils.cleanLabelFR(tripHeadsign);
	}

	@Override
	public String cleanStopName(String gStopName) {
		return CleanUtils.cleanLabelFR(gStopName);
	}
}
