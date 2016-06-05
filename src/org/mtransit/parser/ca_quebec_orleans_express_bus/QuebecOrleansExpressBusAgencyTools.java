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

	private static final String RID_7 = "7";
	private static final String RID_6 = "6";
	private static final String RID_5 = "5";
	private static final String RID_4 = "4";
	private static final String RID_3 = "3";
	private static final String RID_2 = "2";
	private static final String RID_1 = "1";


	@Override
	public String getRouteShortName(GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteShortName())) {
			if (RID_1.equals(gRoute.getRouteId())) {
				return "MT QC S"; // Montréal - Québec ( Express )
			} else if (RID_2.equals(gRoute.getRouteId())) {
				return "MT YUL"; // Montréal - Aéroport Montréal-Trudeau
			} else if (RID_3.equals(gRoute.getRouteId())) {
				return "QC RK"; // Bas-Saint-Laurent
			} else if (RID_4.equals(gRoute.getRouteId())) {
				return "RK GS S"; // Gaspésie ( Côté Sud De La Péninsule )
			} else if (RID_5.equals(gRoute.getRouteId())) {
				return "RK GS N"; // Gaspésie ( Côté Nord De La Péninsule )
			} else if (RID_6.equals(gRoute.getRouteId())) {
				return "MT QC N"; // Montréal - Québec ( Mauricie )
			} else if (RID_7.equals(gRoute.getRouteId())) {
				return "MT VT"; // Centre-du-Québec
			}
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
		if (RID_1.equals(gRoute.getRouteId())) return "4E76BA";
		if (RID_2.equals(gRoute.getRouteId())) return "4E76BA";
		if (RID_3.equals(gRoute.getRouteId())) return "BF2026";
		if (RID_4.equals(gRoute.getRouteId())) return "69BD45";
		if (RID_5.equals(gRoute.getRouteId())) return "69BD45";
		if (RID_6.equals(gRoute.getRouteId())) return "F89843";
		if (RID_7.equals(gRoute.getRouteId())) return "01ADB9";
		return super.getRouteColor(gRoute);
	}

	private static final String MONTRÉAL = "Montréal";
	private static final String RIMOUSKI = "Rimouski";
	private static final String QUÉBEC = "Québec";
	private static final String AÉROPORT_TRUDEAU = "Aéroport-Trudeau";
	private static final String TRUDEAU = "Trudeau";
	private static final String RIVIERE_DU_LOUP = "Rivière-du-Loup";
	private static final String VICTORIAVILLE = "Victoriaville";

	private static final int INBOUND = 0;

	private static final int OUTBOUND = 1;

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		String stationName = cleanTripHeadsign(gTrip.getTripHeadsign());
		int directionId = gTrip.getDirectionId() == null ? 0 : gTrip.getDirectionId();
		int routeId = (int) mRoute.getId();
		switch (routeId) {
		case 1:
			if (MONTRÉAL.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 2:
			if (gTrip.getTripHeadsign().contains(TRUDEAU)) {
				stationName = AÉROPORT_TRUDEAU;
				directionId = OUTBOUND;
			} else {
				directionId = INBOUND;
			}
			break;
		case 3:
			if (MONTRÉAL.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 4:
			if (RIMOUSKI.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 5:
			if (RIMOUSKI.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 6:
			if (MONTRÉAL.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		case 7:
			if (MONTRÉAL.equals(gTrip.getTripHeadsign())) {
				directionId = INBOUND;
			} else {
				directionId = OUTBOUND;
			}
			break;
		default:
			System.out.printf("\nUnexpected trio to split %s!\n", gTrip);
			directionId = -1;
			System.exit(-1);
			return ;
		}
		mTrip.setHeadsignString(stationName, directionId);
	}

	@Override
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
		if (mTrip.getRouteId() == 1l) {
			if (mTrip.getHeadsignId() == OUTBOUND) {
				mTrip.setHeadsignString(QUÉBEC, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 3l) {
			if (mTrip.getHeadsignId() == OUTBOUND) {
				mTrip.setHeadsignString(RIVIERE_DU_LOUP, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 7l) {
			if (mTrip.getHeadsignId() == OUTBOUND) {
				mTrip.setHeadsignString(VICTORIAVILLE, mTrip.getHeadsignId());
				return true;
			}
		}
		System.out.printf("\nUnexptected trips to merge %s & %s!\n", mTrip, mTripToMerge);
		System.exit(-1);
		return false;
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
