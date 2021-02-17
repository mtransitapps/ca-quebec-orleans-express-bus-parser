package org.mtransit.parser.ca_quebec_orleans_express_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CleanUtils;
import org.mtransit.commons.StringUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

// https://gtfs.keolis.ca/gtfs.zip
public class QuebecOrleansExpressBusAgencyTools extends DefaultAgencyTools {

	public static void main(@NotNull String[] args) {
		new QuebecOrleansExpressBusAgencyTools().start(args);
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "Orleans Express";
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@NotNull
	@Override
	public String cleanRouteLongName(@NotNull String routeLongName) {
		routeLongName = CleanUtils.SAINT.matcher(routeLongName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESIS1.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESIS1_REPLACEMENT);
		routeLongName = CleanUtils.CLEAN_PARENTHESIS2.matcher(routeLongName).replaceAll(CleanUtils.CLEAN_PARENTHESIS2_REPLACEMENT);
		return CleanUtils.cleanLabel(routeLongName);
	}

	private static final String RID_1 = "1";
	private static final String RID_2 = "2";
	private static final String RID_3 = "3";
	private static final String RID_4 = "4";
	private static final String RID_5 = "5";
	private static final String RID_6 = "6";
	private static final String RID_7 = "7";

	@Nullable
	@Override
	public String getRouteShortName(@NotNull GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteShortName())) {
			//noinspection deprecation
			final String routeId = gRoute.getRouteId();
			switch (routeId) {
			case RID_1:
				return "MT QC S"; // Montréal - Québec ( Express )
			case RID_2:
				return "MT YUL"; // Montréal - Aéroport Montréal-Trudeau
			case RID_3:
				return "QC RK"; // Bas-Saint-Laurent
			case RID_4:
				return "RK GS S"; // Gaspésie ( Côté Sud De La Péninsule )
			case RID_5:
				return "RK GS N"; // Gaspésie ( Côté Nord De La Péninsule )
			case RID_6:
				return "MT QC N"; // Montréal - Québec ( Mauricie )
			case RID_7:
				return "MT VT"; // Centre-du-Québec
			}
		}
		return super.getRouteShortName(gRoute);
	}

	private static final String AGENCY_COLOR = "01ADB9";

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Nullable
	@Override
	public String getRouteColor(@NotNull GRoute gRoute) {
		if (StringUtils.isEmpty(gRoute.getRouteColor())) {
			//noinspection deprecation
			final String routeId = gRoute.getRouteId();
			if (RID_1.equals(routeId)) return "4E76BA";
			if (RID_2.equals(routeId)) return "4E76BA";
			if (RID_3.equals(routeId)) return "BF2026";
			if (RID_4.equals(routeId)) return "69BD45";
			if (RID_5.equals(routeId)) return "69BD45";
			if (RID_6.equals(routeId)) return "F89843";
			if (RID_7.equals(routeId)) return "01ADB9";
		}
		return super.getRouteColor(gRoute);
	}

	private static final String MONTREAL = "Montréal";
	private static final String CENTRE_VILLE = "Centre-Ville";
	private static final String MONTREAL_CENTRE_VILLE = MONTREAL + " (" + "Centre Ville" + ")";
	private static final String MONTREAL_CENTRE_VILLE_2 = MONTREAL + " (" + "centre-ville" + ")";
	private static final String AEROPORT_TRUDEAU = "Aéroport Trudeau";
	private static final String MONTREAL_AEROPORT_TRUDEAU = MONTREAL + " (" + AEROPORT_TRUDEAU + ")";
	private static final String QUEBEC = "Québec";
	private static final String UNIVERSITE_LAVAL = "Université Laval";
	private static final String QUEBEC_CENTRE_VILLE = QUEBEC + " (" + CENTRE_VILLE + ")";
	private static final String QUEBEC_SAINTE_FOY = QUEBEC + " (" + "Sainte-Foy" + ")";
	private static final String QUEBEC_UNIVERSITE_LAVAL = QUEBEC + " (" + UNIVERSITE_LAVAL + ")";
	private static final String RIMOUSKI = "Rimouski";
	private static final String GASPE = "Gaspé";
	private static final String RIVIERE_DU_LOUP = "Rivière-Du-Loup";
	private static final String VICTORIAVILLE = "Victoriaville";
	private static final String TROIS_RIVIERES = "Trois-Rivières";

	private static final int INBOUND = 0;

	private static final int OUTBOUND = 1;

	@SuppressWarnings("DuplicateBranchesInSwitch")
	@Override
	public void setTripHeadsign(@NotNull MRoute mRoute, @NotNull MTrip mTrip, @NotNull GTrip gTrip, @NotNull GSpec gtfs) {
		int routeId = (int) mRoute.getId();
		switch (routeId) {
		case 1:
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())
					|| MONTREAL_CENTRE_VILLE_2.equalsIgnoreCase(gTrip.getTripHeadsign())) {
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
			} else if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())
					|| MONTREAL_CENTRE_VILLE_2.equalsIgnoreCase(gTrip.getTripHeadsign())
					|| QUEBEC_SAINTE_FOY.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			}
			break;
		case 3:
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign()) //
					|| MONTREAL_CENTRE_VILLE_2.equalsIgnoreCase(gTrip.getTripHeadsign())
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
			if (RIMOUSKI.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (GASPE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		case 6:
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())
					|| MONTREAL_CENTRE_VILLE_2.equalsIgnoreCase(gTrip.getTripHeadsign())) {
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
			if (MONTREAL_CENTRE_VILLE.equalsIgnoreCase(gTrip.getTripHeadsign())
					|| MONTREAL_CENTRE_VILLE_2.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), INBOUND);
				return;
			} else if (VICTORIAVILLE.equalsIgnoreCase(gTrip.getTripHeadsign())) {
				mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), OUTBOUND);
				return;
			}
			break;
		}
		throw new MTLog.Fatal("Unexpected trip to split %s!", gTrip.toStringPlus());
	}

	@Override
	public boolean directionFinderEnabled() {
		return false; // DISABLED because direction_id NOT provided
	}

	@Override
	public boolean mergeHeadsign(@NotNull MTrip mTrip, @NotNull MTrip mTripToMerge) {
		List<String> headsignsValues = Arrays.asList(mTrip.getHeadsignValue(), mTripToMerge.getHeadsignValue());
		if (mTrip.getRouteId() == 1L) {
			if (Arrays.asList( //
					UNIVERSITE_LAVAL, //
					QUEBEC // ++
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(QUEBEC, mTrip.getHeadsignId());
				return true;
			}
		} else if (mTrip.getRouteId() == 2L) {
			if (Arrays.asList( //
					"Québec (Ste-Foy)", //
					MONTREAL //
			).containsAll(headsignsValues)) {
				mTrip.setHeadsignString(MONTREAL, mTrip.getHeadsignId()); // Québec (Ste-Foy)
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
		throw new MTLog.Fatal("Unexpected trips to merge %s & %s!", mTrip, mTripToMerge);
	}

	private static final Pattern CENTRE_VILLE_ = Pattern.compile("((^|\\W)(centre ville)(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String CENTRE_VILLE_REPLACEMENT = "$2" + CENTRE_VILLE + "$4";

	private static final Pattern MONTREAL_CENTRE_VILLE_ = Pattern
			.compile("((^|\\W)(montr[é|e]al \\(centre-ville\\))(\\W|$))", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final String MONTREAL_CENTRE_VILLE_REPLACEMENT = "$2" + MONTREAL + "$4";

	private static final Pattern QUEBEC_CENTRE_VILLE_ = Pattern.compile("((^|\\W)(qu[é|e]bec \\(centre-ville\\))(\\W|$))", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final String QUEBEC_CENTRE_VILLE_REPLACEMENT = "$2" + QUEBEC + "$4";

	private static final Pattern QUEBEC_UNIVERSITE_LAVAL_ = Pattern.compile("((^|\\W)(qu[é|e]bec \\(université laval\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.CANON_EQ);
	private static final String QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT = "$2" + UNIVERSITE_LAVAL + "$4";

	private static final Pattern MONTREAL_AEROPORT_TRUDEAU_ = Pattern.compile("((^|\\W)(montr[é|e]al \\(a[é|e]roport trudeau\\))(\\W|$))",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	private static final String MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT = "$2" + AEROPORT_TRUDEAU + "$4";

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = QUEBEC_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(QUEBEC_CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = QUEBEC_UNIVERSITE_LAVAL_.matcher(tripHeadsign).replaceAll(QUEBEC_UNIVERSITE_LAVAL_REPLACEMENT);
		tripHeadsign = MONTREAL_CENTRE_VILLE_.matcher(tripHeadsign).replaceAll(MONTREAL_CENTRE_VILLE_REPLACEMENT);
		tripHeadsign = MONTREAL_AEROPORT_TRUDEAU_.matcher(tripHeadsign).replaceAll(MONTREAL_AEROPORT_TRUDEAU_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanBounds(Locale.FRENCH, tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypesFRCA(tripHeadsign);
		return CleanUtils.cleanLabelFR(tripHeadsign);
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.cleanBounds(Locale.FRENCH, gStopName);
		gStopName = CleanUtils.cleanStreetTypesFRCA(gStopName);
		return CleanUtils.cleanLabelFR(gStopName);
	}
}
