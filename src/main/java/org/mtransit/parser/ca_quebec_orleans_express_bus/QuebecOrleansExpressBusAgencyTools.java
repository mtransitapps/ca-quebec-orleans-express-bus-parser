package org.mtransit.parser.ca_quebec_orleans_express_bus;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.gtfs.data.GTripStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;
import org.mtransit.parser.mt.data.MTripStop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

// https://gtfs.keolis.ca/gtfs.zip
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
		MTLog.log("Generating Orleans Express bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		MTLog.log("Generating Orleans Express bus data... DONE in %s.", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIds != null && this.serviceIds.isEmpty();
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

	private static final String RID_1 = "1";
	private static final String RID_2 = "2";
	private static final String RID_3 = "3";
	private static final String RID_4 = "4";
	private static final String RID_5 = "5";
	private static final String RID_6 = "6";
	private static final String RID_7 = "7";

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

	private static final String MONTREAL = "Montréal";
	private static final String CENTRE_VILLE = "Centre-Ville";
	private static final String MONTREAL_CENTRE_VILLE = MONTREAL + " (" + "Centre Ville" + ")";
	private static final String AEROPORT_TRUDEAU = "Aéroport Trudeau";
	private static final String MONTREAL_AEROPORT_TRUDEAU = MONTREAL + " (" + AEROPORT_TRUDEAU + ")";
	private static final String QUEBEC = "Québec";
	private static final String UNIVERSITE_LAVAL = "Université Laval";
	private static final String QUEBEC_CENTRE_VILLE = QUEBEC + " (" + CENTRE_VILLE + ")";
	private static final String QUEBEC_UNIVERSITE_LAVAL = QUEBEC + " (" + UNIVERSITE_LAVAL + ")";
	private static final String RIMOUSKI = "Rimouski";
	private static final String GASPE = "Gaspé";
	private static final String RIVIERE_DU_LOUP = "Rivière-Du-Loup";
	private static final String VICTORIAVILLE = "Victoriaville";
	private static final String TROIS_RIVIERES = "Trois-Rivières";

	private static final int INBOUND = 0;

	private static final int OUTBOUND = 1;

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;

	static {
		//noinspection UnnecessaryLocalVariable
		HashMap<Long, RouteTripSpec> map2 = new HashMap<>();
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop, this);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@Override
	public ArrayList<MTrip> splitTrip(MRoute mRoute, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@Override
	public Pair<Long[], Integer[]> splitTripStop(MRoute mRoute, GTrip gTrip, GTripStop gTripStop, ArrayList<MTrip> splitTrips, GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()), this);
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		int routeId = (int) mRoute.getId();
		switch (routeId) {
		case 1:
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (QUEBEC_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign()) //
					|| QUEBEC_UNIVERSITE_LAVAL.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		case 2:
			if (MONTREAL_AEROPORT_TRUDEAU.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			} else if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			}
			break;
		case 3:
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign()) //
					|| QUEBEC_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (RIMOUSKI.equalsIgnoreCase(gTrip.getTripHeadsign()) //
					|| RIVIERE_DU_LOUP.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		case 4:
			if (RIMOUSKI.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (GASPE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		case 5:
			//noinspection DuplicateBranchesInSwitch
			if (RIMOUSKI.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (GASPE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		case 6:
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (QUEBEC_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			} else if (TROIS_RIVIERES.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		case 7:
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (VICTORIAVILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		}
		MTLog.logFatal("Unexpected trip to split %s!", gTrip);
	}

	@Override
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
		List<String> headsignsValues = Arrays.asList(mTrip.getHeadsignValue(), mTripToMerge.getHeadsignValue());
		if (mTrip.getRouteId() == 1L) {
			if (Arrays.asList( //
					UNIVERSITE_LAVAL, //
					QUEBEC // ++
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(QUEBEC, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 3L) {
			if (Arrays.asList( //
					RIVIERE_DU_LOUP, //
					RIMOUSKI //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(RIMOUSKI, mTrip.getHeadsignId());
				return true;
			} else if (Arrays.asList( //
					QUEBEC, //
					MONTREAL // ++
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(MONTREAL, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 6L) {
			if (Arrays.asList( //
					TROIS_RIVIERES, //
					QUEBEC //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(QUEBEC, mTrip.getHeadsignId());
				return true;
			}
		}
		MTLog.logFatal("Unexpected trips to merge %s & %s!", mTrip, mTripToMerge);
		return false;
	}

	private static final Pattern CENTRE_VILLE_ = Pattern.compile("((^|\\W)(centre ville)(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String CENTRE_VILLE_REPLACEMENT = "$2" + CENTRE_VILLE + "$4";

	private static final Pattern MONTREAL_CENTRE_VILLE_ = Pattern
			.compile("((^|\\W)(montr[é|e]al \\(centre-ville\\))(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String MONTREAL_CENTRE_VILLE_REPLACEMENT = "$2" + MONTREAL + "$4";

	private static final Pattern QUEBEC_CENTRE_VILLE_ = Pattern.compile("((^|\\W)(qu[é|e]bec \\(centre-ville\\))(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String QUEBEC_CENTRE_VILLE_REPLACEMENT = "$2" + QUEBEC + "$4";

	private static final Pattern QUEBEC_UNIVERSITE_LAVAL_ = Pattern.compile("((^|\\W)(qu[é|e]bec \\(université laval\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE);
	private static final String QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT = "$2" + UNIVERSITE_LAVAL + "$4";

	private static final Pattern MONTREAL_AEROPORT_TRUDEAU_ = Pattern.compile("((^|\\W)(montr[é|e]al \\(a[é|e]roport trudeau\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE);
	private static final String MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT = "$2" + AEROPORT_TRUDEAU + "$4";

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = QUEBEC_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(QUEBEC_CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = QUEBEC_UNIVERSITE_LAVAL_.matcher(tripHeadsign).replaceAll(QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT);
		tripHeadsign = MONTREAL_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(MONTREAL_CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = MONTREAL_AEROPORT_TRUDEAU_.matcher(tripHeadsign).replaceAll(MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT);
		return CleanUtils.cleanLabelFR(tripHeadsign);
	}

	@Override
	public String cleanStopName(String gStopName) {
		return CleanUtils.cleanLabelFR(gStopName);
	}
}
